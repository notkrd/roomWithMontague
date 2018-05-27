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
                 val lexicon: Map[KeyPhrase, Set[KeyPhrase]] = Map(),
                 val name: String = "unknown world",
                 val m_triggers: Map[String, Monologue] = Map(),
                 val m_syntax: Map[(String, String), String] = shit_syntax
                ) extends World(entities, relations1, relations2){

  def possSuccs(a_cat: String): Set[String] = {
    m_syntax.keySet.filter( lr => lr._1 == a_cat).map( lr => lr._2)
  }

  def charSet(p: PredSing): Set[Entity] = entities_set.filter(p)

  def charList(p: PredSing): List[Entity] = charSet(p).toList

  def binCharSet(p: PredBin): Set[(Entity, Entity)] = {
    val pairs: Set[(Entity, Entity)] = for {
      x <- entities_set
      y <- entities_set
    } yield (x, y)

    pairs.filter( v => p(v._1)(v._2))
  }

  def concatPhrase(parsed: List[Map[String, String]]): String = parsed.foldLeft("")((s, p) => s + p("phrase") + " ").toLowerCase.trim

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
  def key_error_for(phr_in: String): String =  "In saying <strong>" + phr_in + "</strong>, you or the programmer seem to have invented words, which is forbidden in the strongest terms. "

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

  def parse_error_for(parse_error: String)(phr_in: String): String = "At <strong>" + phr_in + "</strong> the machine spins out, elsewhere, grinding. It leaves these notes: " + parse_error

  val nothing_msg = "There is nothing here, unuttered. "

  /* Now we go in for the parser combinators, wish me luck! */

  trait DiscourseParser extends Parsers {
    type Elem = (String, String)

    class SynReader(phrases: List[(String, String)]) extends Reader[Elem] {
      def first: Elem = phrases.head
      def rest: Reader[Elem] = new SynReader(phrases.tail)
      def atEnd: Boolean = phrases.isEmpty
      def pos: Position = NoPosition

      val full_phrase: String = phrases.foldLeft("")((phrase_made, fst_phr) => phrase_made + " " + fst_phr._2)
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

    /* Compound / complex parsers */

    def mod_with(p1: PredSing)(p2: PredSing): PredSing = {
      (charSet(p1) intersect charSet(p2)) contains _
    }

    val PredModParser: Parser[PredSing] => PredSing => Parser[PredSing] = p => mod => {
      p ^^ mod_with(mod)
    }

    val ANParser: Parser[PredSing] = (AdjTokenParser >> PredModParser(NTokenParser)) | (AdjTokenParser >> PredModParser(ANParser))


    val NParser: Parser[PredSing] = NTokenParser | ANParser

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
              case _ => Success(detThe(), in.rest)
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

    val NPParser: Parser[Entity] = NPTokenParser | DetNParser

    def VTPartialApp: PredBin => Parser[PredSing] = pred => NPParser ^^ (e => pred(e))

    val VTNPParser: Parser[PredSing] = VTTokenParser >> VTPartialApp

    abstract class Auxiliary
    case class AuxIs() extends Auxiliary

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
              case _ => Success(AuxIs(), in.rest)
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

    val AuxNPParser: Parser[PredSing] = NPParser ^^ (ent => _ == ent)

    val AuxAdjParser: Parser[PredSing] = AdjTokenParser ^^ (adj => e => adj(e))

    val auxMeaning: Auxiliary => Parser[PredSing] = {
      case _: AuxIs => AuxAdjParser | AuxNPParser
      case _ => AuxAdjParser | AuxNPParser
    }

    val auxPhrParser: Parser[PredSing] = AuxTokenParser >> auxMeaning

    val VPParser: Parser[PredSing] = VITokenParser | VTNPParser | auxPhrParser

    val ApplyVP: Entity => Parser[Boolean] = ent => VPParser ^^ (vp => vp(ent))

    val SParser: Parser[Boolean] = NPParser >> ApplyVP

  }

  object MontagueParser extends DiscourseParser {
    val sample_in = new SynReader(List(("Determiner", "the"),("Noun", "mathematician"),("Intransitive Verb", "lives")))
    val sample_adjs = new SynReader(List(("Determiner", "the"),("Adjective", "thick"),("Adjective", "red"),("Noun", "book"),("Intransitive Verb", "lives")))

    def eval_phrases(phrs: List[Map[String, String]]): ParseResult[Boolean] = SParser(mapToReader(phrs))

    def comment_on(phrs: List[Map[String, String]]): String = {
      val full_phrase = concatPhrase(phrs)
      eval_phrases(phrs) match {
        case Success(v, _) => {
          v match {
            case true => truth_for(full_phrase)
            case false => falsity_for(full_phrase)
          }
        }
        case Failure(err, _) => failure_for(full_phrase, err)
        case Error(err, _) => parse_error_for(err)(full_phrase)
        }
      }
    }

  /* Combinators over */

  /** Evaluates an utterance uses parsing combinators
    *
    * @param phrase_maps a list of phrases, where each map has "cat" and "phrase" keys
    * @return A string response using the evaluation
    */
  def combinatorsParse(phrase_maps: List[Map[String, String]]): String = {
    MontagueParser.comment_on(phrase_maps)
  }

  /** Evaluate a sentence to provide a response to give the reader
    *
    * @param parsed phrase to evaluate, as a sequence of tagged phrases
    * @return Message corresponding to the parse
    */
  def evalSent(parsed: List[Map[String, String]]): String = {
    val full_phrase: String = parsed.foldLeft("")((s, p) => s + p("phrase") + " ").toLowerCase.trim
    val response: String = if(m_triggers.keySet contains full_phrase) {
      combinatorsParse(parsed) ++ "<br><br>" ++ m_triggers(full_phrase).text
    }
    else {
      combinatorsParse(parsed)
    }
      response + "</br></br>"
  }

}
