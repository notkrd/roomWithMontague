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

  val shit_syntax: Map[(String, String), String] = Map(("Vacant", "Vacant") -> "Vacant", ("Vacant", "Entity") -> "Entity", ("Vacant", "Determiner") -> "Determiner", ("Determiner", "Noun") -> "Entity", ("Entity", "Intransitive Verb") -> "Sentence", ("Entity", "Auxiliary Verb") -> "Entity + Auxiliary Verb", ("Entity + Auxiliary Verb", "Adjective") -> "Sentence", ("Entity + Auxiliary Verb", "Entity") -> "Sentence", ("Entity", "Transitive Verb") -> "Entity + Transitive Verb", ("Entity + Transitive Verb", "Entity") -> "Sentence")
}