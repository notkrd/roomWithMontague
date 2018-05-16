package models

import scala.collection.immutable.{Seq, Set}

object thisRoom{
  val d_entities: Map[KeyPhrase, Entity] = Map("the rug" -> "BLUE_RUG", "the window" -> "WINDOW", "the peel" -> "ORANGE_PEEL", "the ant" -> "ANT", "the dirty dishes" -> "DIRTY_DISHES", "the deer" -> "DEER", "the person" -> "PERSON", "the kettle" -> "KETTLE", "the nonempty cup" -> "NONEMPTY_CUP", "the empty blue cup" -> "BLUE_CUP", "the empty glass" -> "EMPTY_GLASS", "the water" -> "CUP_WATER", "the towel" -> "TOWEL", "the walls" -> "WALLS", "the spider" -> "SPIDER", "the books" -> "BOOKS", "the thick red paperback" -> "DHALGREN", "the textbook" -> "LINGUISTICS_TEXTBOOK")

  val isAlive: PredSing = Set("ANT", "PERSON", "DEER")
  val isWet: PredSing = Set("CUP_WATER", "NONEMPTY_CUP", "TOWEL")
  val isDish: PredSing = Set("CUP_WATER", "NONEMPTY_CUP", "EMPTY_GLASS", "DIRTY_DISHES")
  val isSolid: PredSing = Set("CUP_WATER", "NONEMPTY_CUP", "EMPTY_GLASS", "WINDOW", "WALLS", "KETTLE")
  val isDirty: PredSing = Set("DIRTY_DISHES", "BLUE_RUG")
  val isEmpty: PredSing = Set("EMPTY_GLASS", "BLUE_CUP", "ORANGE_PEEL", "PERSON")
  val isDead: PredSing = Set("SPIDER")
  val isBlue: PredSing = Set("BLUE_CUP", "BLUE_RUG")
  val isRed: PredSing = Set("DHALGREN")
  val isThick: PredSing = Set("DHALGREN")
  val isCup: PredSing = Set("BLUE_CUP", "EMPTY_GLASS", "CUP_WATER")
  val isAnimal: PredSing = Set("ANT", "PERSON", "DEER", "SPIDER")

  val seeTuples: Set[Tuple2[Entity,Entity]] = (for {a_thing <- d_entities.values} yield ("AUTHOR", a_thing)).toSet + (("ANT","AUTHOR"))
  val doesSee: PredBin = tuplesToPredBin(seeTuples)

  val d_rel1: Map[KeyPhrase, PredSing] = Map("lives" -> isAlive, "alive" -> isAlive, "wet" -> isWet, "dish" -> isDish, "dead" -> isDead, "dirty" -> isDirty, "empty" -> isEmpty, "blue" -> isBlue, "animal" -> isAnimal, "cup" -> isCup, "red" -> isRed, "thick" -> isThick)
  val d_rel2: Map[KeyPhrase, PredBin] = Map("sees" -> doesSee)

  var l_det = Set("a", "the")
  var l_verbs = Set("lives")
  var l_adjs = Set("wet", "dead", "alive", "empty", "blue", "dirty", "red", "thick")
  var l_ns = Set("dish", "animal", "cup")
  var l_spec = Set("is","not","and","or")
  var a_lex = Map("Entity" -> d_entities.keySet, "Intransitive Verb" -> l_verbs, "Transitive Verb" -> d_rel2.keySet, "Special" -> l_spec, "Adjective" -> l_adjs, "Noun" -> l_ns, "Determiner" -> l_det)

  val discRepresentation = new Box(Seq(), Seq())

  val d_model: World = new World(d_entities, d_rel1, d_rel2, a_lex, "This room")
}