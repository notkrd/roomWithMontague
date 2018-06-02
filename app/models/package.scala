import scala.collection.immutable.Set

/** Relevant type synonyms. */
package object models {
  type KeyPhrase = String
  type Variable = String
  type Entity = String
  type Referent = Either[Variable, KeyPhrase]
  type Utterance = String
  type PredSing = Entity => Boolean
  type PredBin = Entity => Entity => Boolean
  type Embedding = Map[Variable, Entity]
  type Lexicon[A] = Map[A, Set[Utterance]]

  /** Reduces a referent to underlying string
    *
    * @param r The referent in
    * @return The string
    */
  implicit def RefVar(r: Referent): Variable = r match {
    case Left(v) => v
    case Right(v) => v
  }

  /** Converts a set of tuples to a binary relation
    *
    * @param tups The ordered pairs satisfying the predicate
    * @return The characteristic binary relation for the set of pairs
    */
  def tuplesToPredBin(tups: Set[(Entity, Entity)]): PredBin = (x: Entity) => (y: Entity) => {
    tups.contains((x, y))
  }

  def formatStr(str: String): String = {
    str.trim.capitalize ++ ". "
  }

  def wrapPhrase(cat: String)(phr: String): List[(String, String)] = List[(String, String)]((cat, phr))

  def wrapPhraseSet(cat: String)(phrases: Set[String]): (String, Set[List[(String, String)]]) = {
    (cat, phrases.map(wrapPhrase(cat)))
  }

  def toOnePhrase(phrases: List[(String, String)]): String = phrases.foldLeft("")((s: String, a_phr: (String, String)) => s + s" ${a_phr._2}").trim

  def jsonifyPhrases(phrases: List[(String, String)]): String =  s"[${phrases.foldLeft("")((j: String, phr: (String, String)) => j + s"{\042cat\042: \042${phr._1}\042, \042phrase\042: \042${phr._2}\042}, ").dropRight(2)}]"

  def escapeKey(some_key: String): String = {
    some_key.replaceAll(" ","_")
  }

  val shit_syntax: Map[(String, String), String] = Map(
    ("Vacant", "Vacant") -> "Vacant", ("Vacant", "Entity") -> "Entity",
    ("Vacant", "Determiner") -> "Determiner",
    ("Determiner", "Noun") -> "Entity",
    ("Determiner", "Adjective") -> "Awaits Noun",
    ("Awaits Noun", "Noun") -> "Entity",
    ("Awaits Noun", "Adjective") -> "Awaits Noun",
    ("Entity", "Intransitive Verb") -> "Sentence",
    ("Entity", "Auxiliary Verb") -> "Entity + Auxiliary Verb",
    ("Entity + Auxiliary Verb", "Adjective") -> "Sentence",
    ("Entity + Auxiliary Verb", "Entity") -> "Sentence",
    ("Entity + Auxiliary Verb", "Determiner") -> "Determiner Final",
    ("Determiner Final", "Noun") -> "Sentence",
    ("Determiner Final", "Adjective") -> "Awaits Noun Final",
    ("Awaits Noun Final", "Noun") -> "Sentence",
    ("Awaits Noun Final", "Adjective") -> "Awaits Noun Final",
    ("Entity", "Transitive Verb") -> "Entity + Transitive Verb",
    ("Entity + Transitive Verb", "Entity") -> "Sentence")

  val prediction_pairs: Map[String, Set[String]] = Map(
    "Vacant" -> Set("Vacant", "Entity", "Determiner"),
    "Determiner" -> Set( "Noun", "Adjective"),
    "Adjective" -> Set("Adjective", "Noun"),
    "Noun" -> Set("Auxiliary Verb", "Intransitive Verb", "Transitive Verb"),
    "Auxiliary Verb" -> Set("Determiner", "Adjective"),
    "Transitive Verb" -> Set("Determiner", "Entity"))
}