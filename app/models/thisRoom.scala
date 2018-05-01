package models

import scala.collection.immutable.{Seq, Set}

object thisRoom{
  val d_entities: Map[KeyPhrase, Entity] = Map("the rug" -> "BLUE_RUG", "the window" -> "WINDOW", "the peel" -> "ORANGE_PEEL", "the ant" -> "ANT", "the dirty dishes" -> "DIRTY_DISHES", "I" -> "AUTHOR", "the deer" -> "DEER", "the person" -> "PERSON", "the kettle" -> "KETTLE", "the nonempty cup" -> "NONEMPTY_CUP", "the empty blue cup" -> "BLUE_CUP", "the empty glass" -> "EMPTY_GLASS", "the water" -> "CUP_WATER", "the towel" -> "TOWEL")

  val isAlive: PredSing = Set("ANT", "AUTHOR", "DEER")
  val isWet: PredSing = Set("CUP_WATER", "NONEMPTY_CUP", "TOWEL")
  val isDish: PredSing = Set("CUP_WATER", "NONEMPTY_CUP", "EMPTY_GLASS", "DIRTY_DISHES")
  val isDead: PredSing = Set()

  val seeTuples: Set[Tuple2[Entity,Entity]] = (for {a_thing <- d_entities.values} yield ("AUTHOR", a_thing)).toSet + (("ANT","AUTHOR"))
  val doesSee: PredBin = tuplesToPredBin(seeTuples)

  val d_rel1: Map[KeyPhrase, PredSing] = Map("lives" -> isAlive, "wet" -> isWet, "dish" -> isDish, "dead" -> isDead)
  val d_rel2: Map[KeyPhrase, PredBin] = Map("sees" -> doesSee)

  var l_verbs = Set("lives")
  var l_adjs = Set("wet", "dead")
  var l_ns = Set("dish")
  var aux_verbs = Set("is")
  var a_lex = Map("Entities" -> d_entities.keySet, "Verbs" -> l_verbs, "Transitive Verbs" -> d_rel2.keySet, "Auxiliary Verbs" -> aux_verbs, "Adjectives" -> l_adjs, "Nouns" -> l_ns)

  val discRepresentation = new Box(Seq(), Seq())

  val d_model: World = new World(d_entities, d_rel1, d_rel2, a_lex)
}