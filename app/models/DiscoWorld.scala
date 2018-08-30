package models

import scala.collection.immutable._
import play.api.Logger
import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{Reader, Position, NoPosition}

/** Object for a "room" in the fiction, to be described.
  *
  * @param entities a partial function from names to individuals
  * @param relations1 a partial function from words to predicates in the model, where a predicate is a from entitie to truth values
  * @param relations2 a partial function from words to binary relations in the model
  * @param lexicon the words in the model, found by categories, useable to speak something
  * @param name naming it something lets us bundle it up, choose among alternatives
  * @param m_triggers phrases inciting monologues, with the monologues incited
  * @param m_syntax phrase-structure style composition rules. Not currently used for parsing, but currently just to highlight permissible categories
  */
class DiscoWorld(entities: Map[KeyPhrase, Entity],
                 relations1: Map[KeyPhrase, PredSing],
                 relations2: Map[KeyPhrase, Entity => Entity => Boolean],
                 val lexicon: PhrasesLexicon = Map[KeyPhrase, Set[List[(KeyPhrase, String)]]](),
                 val name: String = "unknown world",
                 val m_triggers: Map[String, Monologue] = Map(),
                 val m_syntax: Map[(String, String), String] = shit_syntax,
                 val epigraph: String = ""
                ) extends World(entities, relations1, relations2){

  def possSuccs(a_cat: String): Set[String] = {
    m_syntax.keySet.filter( lr => lr._1 == a_cat).map( lr => lr._2)
  }

  implicit def charSet(p: PredSing): Set[Entity] = entities_set.filter(p)

  def charList(p: PredSing): List[Entity] = charSet(p).toList

  implicit def binCharSet(p: PredBin): Set[(Entity, Entity)] = {
    val pairs: Set[(Entity, Entity)] = for {
      x <- entities_set
      y <- entities_set
    } yield (x, y)

    pairs.filter( v => p(v._1)(v._2))
  }

  def concatPhrase(parsed: List[Map[String, String]]): String = parsed.foldLeft("")((s, p) => s + p("phrase") + " ").toLowerCase.trim

  def concatFromPairs(pairs: Phrase): String = pairs.foldLeft("")((phrase_made: String, fst_phr: (String, String)) => phrase_made + " " + fst_phr._2)

  /** Message for parse error
    *
    * @param phr_in phrase to parse
    * @return the message
    */
  def failure_for(phr_in: String, err: String = ""): String = "This language does not determine whether or not <strong>" ++ phr_in ++ "</strong>. You must be speaking some other language, if you are speaking language at all. " + err

  /** Message for key error
    *
    * @param phr_in phrase to parse
    * @return the message
    */
  def key_error_for(phr_in: String): String =  "In saying <strong>" + phr_in + "</strong>, you or the programmer seem to have invented (or forgotten to annotate) words, which is forbidden in the strongest terms. "

  /** Message for successful true parse
    *
    * @param phr_in phrase to parse
    * @return the message
    */
  def truth_for(phr_in: String): String = "<strong>" + phr_in.capitalize + "</strong>. "

  /** Message for successful false parse
    *
    * @param phr_in phrase to parse
    * @return the message
    */
  def falsity_for(phr_in: String): String = "It is not the case that <strong>" + phr_in + "</strong>. "

  def parse_error_for(parse_error: String)(phr_in: String): String = "At <strong>" + phr_in + "</strong> the language machine spins out, elsewhere, grinding. It leaves these notes: " + parse_error

  val nothing_msg = "There is nothing here, unuttered. "

  /* Now we go in for the parser combinators, wish me luck! */

  trait DiscourseParser extends Parsers {
    type Elem = (String, String)

    class SynReader(val phrases: Phrase, val phrases_learned: PhrasesLexicon = empty_lex) extends Reader[Elem] {
      def first: Elem = phrases.head
      def rest: SynReader = new SynReader(phrases.tail, phrases_learned)
      def atEnd: Boolean = phrases.isEmpty
      def pos: Position = NoPosition

      val full_phrase: String = concatFromPairs(phrases)

      override def toString(): String = {
        s"Full: $full_phrase, parsed: $phrases, new: $phrases_learned"
      }

      def add_phrase(new_cat: String)(new_phr: List[(String, String)]): SynReader = {
        if (phrases_learned contains new_cat) {
          new SynReader(phrases, phrases_learned + (new_cat -> (phrases_learned(new_cat) + new_phr)))
        }
        else {
          new SynReader(phrases, phrases_learned + (new_cat -> Set(new_phr)))
        }
      }

      def strs_consumed(that: SynReader): String = phrases.take(phrases.length - that.phrases.length).foldLeft("")((s: String, p: (String, String)) => s + " " + p._2)

      /** Get phrases consumed since a previous reader
        *
        * @param that A previous SynReader. This SynReader ought to be a subset of that (unvalidated)
        * @return The list of phrases
        */
      def phrases_consumed(that: SynReader): List[(String, String)] = that.phrases.take(that.phrases.length - this.phrases.length)

      /** Get phrases consumed since a previous reader, and add it to *phrases_learned*
        *
        * @param new_cat syntactic category to add the phrase too
        * @param that A previous SynReader. *This* SynReader ought to be a subset of *that* (unvalidated)
        * @return A SynReader identical except for the addition of the difference from *that* as a phrase of *new_cat*
        */
      def add_difference(new_cat: String)(that: SynReader): SynReader = add_phrase(new_cat)(phrases_consumed(that))

    }

    def unfold_input(some_input: Input): List[(String, String)] = {
      if(some_input.atEnd) {
        List[(String, String)]()
      }
      else {
        some_input.first :: unfold_input(some_input.rest)
      }
    }

    implicit def toSynReader(some_input: Input): SynReader = some_input match {
      case s: SynReader => s
      case i: Input => new SynReader(unfold_input(i))
    }

    /** Takes a list of maps to a list of (cat, phrase) pairs
      *
      * @param some_phrases List of maps corresponding to phrases with entries for "cat" and "phrase"
      * @return List in same order with tagged phrases for all the maps that have the necessary keys
      */
    def mapToReader(some_phrases: List[Map[String, String]]): Input = {
      val valid_phrases: List[Map[String, String]] = some_phrases.filter( phr => Set("cat", "phrase").subsetOf(phr.keySet) )
      val cat_phrase_pairs: List[(String, String)] = valid_phrases.map( phr => (phr("cat"), phr("phrase")))
      new SynReader(cat_phrase_pairs)
    }

    def parseAndLearn[T](cat: String)(p: Parser[T]): Parser[T] = new Parser[T] {
      def apply(in: Input): ParseResult[T] = p(in) match {
        case Success(e, r) => Success(e, r.add_difference(cat)(in))
        case other: NoSuccess => other
      }
    }

    def PredTokenParser(of_cat: String): Parser[PredSing] = new Parser[PredSing] {
      def apply(in: Input): ParseResult[PredSing] = {
        if(!in.atEnd) {
          val h = in.first
          if (h._1 == of_cat) {
            if (relations1.keySet contains h._2) {
              Success(relations1(h._2), in.rest)
            }
            else {
              Error(key_error_for(h._2), in)
            }
          }
          else {
            Failure(failure_for(h._2), in)
          }
        }
        else {
          Failure(nothing_msg, in)
        }
      }
    }

    val NTokenParser: Parser[PredSing] = PredTokenParser("Noun")

    /** Parses an adjective - as a function from PredSing to PredSing
      *
      */
    val AdjTokenParser:  Parser[PredSing] = PredTokenParser("Adjective")

    /** Parses a verb phrase, as a PredSing
      *
      */
    val VITokenParser: Parser[PredSing] = PredTokenParser("Intransitive Verb")

    /** Parses an entity / noun phrase
      *
      */
    val VTTokenParser: Parser[PredBin] = new Parser[PredBin] {
      def apply(in: Input): ParseResult[PredBin] = {
        if(!in.atEnd) {
          val h = in.first
          if (h._1 == "Transitive Verb") {
            if (relations2.keySet contains h._2) {
              Success(relations2(h._2), in.rest)
            }
            else {
              Error(key_error_for(h._2), in)
            }
          }
          else {
            Failure(failure_for(h._2), in)
          }
        }
        else {
          Failure(nothing_msg, in)
        }
      }
    }

    val NPTokenParser: Parser[Entity] = new Parser[Entity] {

      def apply(in: Input): ParseResult[Entity] = {
        if (!in.atEnd) {
          val h = in.first
          if (h._1 == "Entity") {
            if (entities.keySet contains h._2) {
              Success(entities(h._2), in.rest)
            }
            else {
              Error(key_error_for(h._2), in)
            }
          }
          else {
            Failure(failure_for(h._2), in)
          }
        }
        else {
          Failure(nothing_msg, in)
        }
      }
    }

    /* Helper functions */

    val mod_with: PredSing => PredSing => PredSing = p1 => p2 => {
      (p1 intersect p2) contains _
    }

    val combine_combinator: Parser[(PredSing, PredSing) => PredSing] = new Parser[(PredSing, PredSing) => PredSing] {
      def apply(in: Input): ParseResult[(PredSing, PredSing) => PredSing] = Success(Function.uncurried(mod_with), in)
    }

    val predModParser: Parser[PredSing] => PredSing => Parser[PredSing] = pars => p => pars ^^ (mod_with(p)(_))

    /* Compound / complex parsers */

    /** Class for conjunctions. For now, I'm only doing sentence conjunctions
      *
      */
    abstract class Conjunction
    case class conjAnd() extends Conjunction
    case class conjOr() extends Conjunction

    /** Parses a conjunction.
      *
      */
    val ConjTokenParser: Parser[Conjunction] = new Parser[Conjunction] {

      def apply(in: Input): ParseResult[Conjunction] = {
        if (!in.atEnd) {
          val h = in.first
          if (h._1 == "Conjunction") {
            h._2 match {
              case "and" => Success(conjAnd(), in.rest)
              case "or" => Success(conjOr(), in.rest)
              case _ => Success(conjAnd(), in.rest)
            }
          }
          else {
            Failure(failure_for(h._2), in.rest)
          }
        }
        else {
          Failure(nothing_msg, in)
        }
      }
    }

    val conjSMeaning: Conjunction => (Boolean, Boolean) => Boolean = {
      case conjAnd() => (x: Boolean, y: Boolean) => x && y
      case conjOr() => (x: Boolean, y: Boolean) => x || y
    }

    val conjPredMeaning: Conjunction => (PredSing, PredSing) => PredSing = {
      case conjAnd() => (x: PredSing, y: PredSing) => x intersect y
      case conjOr() => (x: PredSing, y: PredSing) => x union y
    }

    val conjPredBinMeaning: Conjunction => (PredBin, PredBin) => PredBin = {
      case conjAnd() => (x: PredBin, y: PredBin) => x intersect y
      case conjOr() => (x: PredBin, y: PredBin) => x union y
    }

    val conjPredParser: Parser[(PredSing, PredSing) => PredSing] = ConjTokenParser ^^ conjPredMeaning

    val conjPredBinParser: Parser[(PredBin, PredBin) => PredBin] = ConjTokenParser ^^ conjPredBinMeaning

    /* Adjectives, nouns */

    val AdjParser: Parser[PredSing] = AdjTokenParser * conjPredParser

    val NParser: Parser[PredSing] = (AdjTokenParser * combine_combinator) >> predModParser(NTokenParser) | NTokenParser

    /** Class for determiners. For now, I'm treating determiners (really only "the") as partial functions from sets to entities
      *
      */
    abstract class Determiner
    case class detThe() extends Determiner
    case class detA() extends Determiner

    val func_the: PartialFunction[PredSing, Entity] = new PartialFunction[PredSing, Entity] {
      def apply(p: PredSing): Entity = charList(p).head
      def isDefinedAt(p: PredSing): Boolean = charList(p).nonEmpty
    }

    val detMeaning: Determiner => Parser[Entity] = {
      case _: detThe => NParser ^? (func_the, (_: PredSing) => "There is nowhere for this \"the\" to point")
      case _ => NParser ^? (func_the, (_: PredSing) => "There is nowhere for this determiner to point")
    }

    /** Parses a determiner.
      *
      */
    val DetTokenParser: Parser[Determiner] = new Parser[Determiner] {

      def apply(in: Input): ParseResult[Determiner] = {
        if (!in.atEnd) {
          val h = in.first
          if (h._1 == "Determiner") {
            h._2 match {
              case "the" => Success(detThe(), in.rest)
              case "a" => Success(detA(), in.rest)
              case "an" => Success(detA(), in.rest)
              case _ => Error(key_error_for(h._2), in.rest)
            }
          }
          else {
            Failure(failure_for(h._2), in.rest)
          }
        }
        else {
          Failure(nothing_msg, in)
        }
      }
    }

    val DetNParser: Parser[Entity] = DetTokenParser >> detMeaning

    val NPParser: Parser[Entity] = parseAndLearn("Entity")(NPTokenParser | DetNParser)

    def VTParser: Parser[PredBin] = VTTokenParser * conjPredBinParser

    def VTPartialApp: PredBin => Parser[PredSing] = pred => NPParser ^^ (e => pred(e))

    val VTNPParser: Parser[PredSing] = VTParser >> VTPartialApp

    abstract class Auxiliary
    case class AuxIs() extends Auxiliary
    case class AuxIsNot() extends Auxiliary

    /** Parses an auxiliary verb.
      *
      */
    val AuxTokenParser: Parser[Auxiliary] = new Parser[Auxiliary] {

      def apply(in: Input): ParseResult[Auxiliary] = {
        if (!in.atEnd) {
          val h = in.first
          if (h._1 == "Auxiliary Verb") {
            h._2 match {
              case "is" => Success(AuxIs(), in.rest)
              case "are" => Success(AuxIs(), in.rest)
              case "is not" => Success(AuxIsNot(), in.rest)
              case _ => Error(key_error_for(h._2), in.rest)
            }
          }
          else {
            Failure(failure_for(h._2), in.rest)
          }
        }
        else {
          Failure(nothing_msg, in)
        }
      }
    }

    def AuxAdjFunc(an_aux: Auxiliary): PredSing => PredSing = an_aux match {
      case AuxIs() => adj => e => adj(e)
      case AuxIsNot() => adj => e => !adj(e)
      case _ => adj => e => adj(e)
    }

    def AuxNPFunc(an_aux: Auxiliary): Entity => PredSing = an_aux match {
      case AuxIs() => ent => _ == ent
      case AuxIsNot() => ent => _ != ent
    }

    val AuxAdjParser: Auxiliary => Parser[PredSing] = an_aux => AdjParser ^^ AuxAdjFunc(an_aux)

    val AuxNPParser: Auxiliary => Parser[PredSing] = an_aux => NPParser ^^ AuxNPFunc(an_aux)

    val auxPhrParser: Parser[PredSing] = (AuxTokenParser >> AuxAdjParser) | (AuxTokenParser >> AuxNPParser)

    val VPParser: Parser[PredSing] = parseAndLearn("Intransitive Verb")((VITokenParser | VTNPParser | auxPhrParser) * conjPredParser)

    val ApplyVP: Entity => Parser[Boolean] = ent => VPParser ^^ (vp => vp(ent))

    /** Class for quantifiers. For now, I'm treating quantifiers as binary functions on PredSings
      *
      */
    abstract class Quantifier
    case class quanSome() extends Quantifier
    case class quanAll() extends Quantifier
    case class quanNo() extends Quantifier

    val QuanTokenParser: Parser[Quantifier] = new Parser[Quantifier] {

      def apply(in: Input): ParseResult[Quantifier] = {
        if (!in.atEnd) {
          val h = in.first
          if (h._1 == "Quantifier") {
            h._2 match {
              case "a" => Success(quanSome(), in.rest)
              case "some" => Success(quanSome(), in.rest)
              case "all" => Success(quanAll(), in.rest)
              case "every" => Success(quanAll(), in.rest)
              case "no" => Success(quanNo(), in.rest)
              case _ => Error(key_error_for(h._2), in.rest)
            }
          }
          else {
            Failure(failure_for(h._2), in.rest)
          }
        }
        else {
          Failure(nothing_msg, in)
        }
      }
    }

    val qMeaning: Quantifier => PredSing => PredSing => Boolean = q => p1 => p2 => q match {
      case quanSome() => (p1 intersect p2).nonEmpty
      case quanAll() => p1 subsetOf p2
      case quanNo() => (p1 intersect p2).isEmpty
    }

    val QuantSParser: Parser[Boolean] = QuanTokenParser >> ((q: Quantifier) => Parser[Boolean]((in: Input) => {
      (NParser ~ VPParser)(in) match {
        case Success(a ~ b, r) => Success(qMeaning(q)(a)(b), r)
        case Failure(fail, r) => Failure(fail, r)
        case Error(err, r) => Error(err, r)
      }
    }))

    val SingleSParser: Parser[Boolean] = (NPParser >> ApplyVP) | QuantSParser

    val conjSParser: Parser[(Boolean, Boolean) => Boolean] = ConjTokenParser ^^ conjSMeaning

    /* Sentence parsers */

    val SParser: Parser[Boolean] = parseAndLearn("Utterance")(SingleSParser * conjSParser)

  }

  object MontagueParser extends DiscourseParser {
    val sample_in = new SynReader(List(("Determiner", "the"),("Noun", "mathematician"),("Intransitive Verb", "lives")))
    val sample_adjs = new SynReader(List(("Determiner", "the"),("Adjective", "thick"),("Adjective", "red"),("Noun", "book"),("Intransitive Verb", "lives")))

    def eval_phrases(phrs: List[Map[String, String]]): ParseResult[Boolean] = SParser(mapToReader(phrs))

    def comment_on(phrs: List[Map[String, String]]): (String, PhrasesLexicon) = {
      val full_phrase = concatPhrase(phrs)
      if (full_phrase.isEmpty) {
        (nothing_msg, empty_lex)
      }
      else {
        eval_phrases(phrs) match {
          case Success(v, r) => {
            v match {
              case true => {
                (truth_for(full_phrase), r.phrases_learned)
              }
              case false => {
                (falsity_for(full_phrase), r.phrases_learned)
              }
            }
          }
          case Failure(err, r) => (failure_for(full_phrase), r.phrases_learned)
          case Error(err, r) => (parse_error_for(err)(full_phrase), r.phrases_learned)
        }
      }
    }
    }

  /* Combinators over */

  /** Evaluates an utterance uses parsing combinators
    *
    * @param phrase_maps a list of phrases, where each map has "cat" and "phrase" keys
    * @return A string response using the evaluation
    */
  def combinatorsParse(phrase_maps: List[Map[String, String]]): (String, PhrasesLexicon) = {
    MontagueParser.comment_on(phrase_maps)
  }

  /** Evaluate a sentence to provide a response to give the reader
    *
    * @param parsed phrase to evaluate, as a sequence of tagged phrases
    * @return Message corresponding to the parse
    */
  def evalSent(parsed: List[Map[String, String]]): (String, PhrasesLexicon) = {
    val full_phrase: String = parsed.foldLeft("")((s, p) => s + p("phrase") + " ").toLowerCase.trim
    val comb_parse = combinatorsParse(parsed)
    (comb_parse._1 + m_triggers.keySet.filter(tr => full_phrase contains tr).foldLeft("")((r: String, tr: String) => {
      s"$r ${m_triggers(tr)} <br><br>"
    }) + "<br><br>", comb_parse._2)
  }

}