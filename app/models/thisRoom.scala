package models

import scala.collection.immutable.{Seq, Set}

object thisRoom{
  val d_entities: Map[KeyPhrase, Entity] = Map("the rug" -> "BLUE_RUG", "the window" -> "WINDOW", "the peel" -> "ORANGE_PEEL", "the ant" -> "ANT", "the dirty dishes" -> "DIRTY_DISHES", "the person" -> "PERSON", "the kettle" -> "KETTLE", "the nonempty cup" -> "NONEMPTY_CUP", "the empty blue cup" -> "BLUE_CUP", "the empty glass" -> "EMPTY_GLASS", "the water" -> "CUP_WATER", "the towel" -> "TOWEL", "the walls" -> "WALLS", "the spider" -> "SPIDER", "the books" -> "BOOKS", "the thick red paperback" -> "DHALGREN", "the textbook" -> "LINGUISTICS_TEXTBOOK", "the blanket" -> "BLANKET", "the screen" -> "SCREEN", "the animal" -> "PERSON", "the cup" -> "BLUE_CUP", "the dirty shirt" -> "SHIRT", "the mathematician" -> "PERSON")

  val isAlive: PredSing = Set("ANT", "PERSON", "DEER")
  val isWet: PredSing = Set("CUP_WATER", "NONEMPTY_CUP", "TOWEL")
  val isDish: PredSing = Set("CUP_WATER", "NONEMPTY_CUP", "EMPTY_GLASS", "DIRTY_DISHES")
  val isSolid: PredSing = Set("CUP_WATER", "NONEMPTY_CUP", "EMPTY_GLASS", "WINDOW", "WALLS", "KETTLE")
  val isDirty: PredSing = Set("DIRTY_DISHES", "BLUE_RUG", "SHIRT", "BLANKET")
  val isEmpty: PredSing = Set("EMPTY_GLASS", "BLUE_CUP", "ORANGE_PEEL", "PERSON")
  val isDead: PredSing = Set("SPIDER")
  val isBlue: PredSing = Set("BLUE_CUP", "BLUE_RUG")
  val isRed: PredSing = Set("DHALGREN")
  val isThick: PredSing = Set("DHALGREN")
  val isCup: PredSing = Set("BLUE_CUP", "EMPTY_GLASS", "CUP_WATER")
  val isWalls: PredSing = Set("WALLS")
  val isBlanket: PredSing = Set("BLANKET")
  val isWindow: PredSing = Set("WINDOW")
  val isAnimal: PredSing = Set("ANT", "PERSON", "SPIDER")
  val isGlowing: PredSing = Set("SCREEN", "WINDOW")
  val isScreen: PredSing = Set("SCREEN")
  val isWater: PredSing = Set("CUP_WATER")
  val isPerson: PredSing = Set("PERSON")
  val isMathematician: PredSing = Set("PERSON")
  val isBook: PredSing = Set("DHALGREN","LINGUISTICS_TEXTBOOK")

  val seeTuples: Set[Tuple2[Entity,Entity]] = (for {a_thing <- d_entities.values} yield (a_thing, "PERSON")).toSet + (("PERSON", "ANT"))
  val doesSee: PredBin = tuplesToPredBin(seeTuples)

  val d_rel1: Map[KeyPhrase, PredSing] = Map("lives" -> isAlive, "alive" -> isAlive, "wet" -> isWet, "dish" -> isDish, "dead" -> isDead, "dirty" -> isDirty, "empty" -> isEmpty, "blue" -> isBlue, "animal" -> isAnimal, "cup" -> isCup, "red" -> isRed, "thick" -> isThick, "glows" -> isGlowing, "walls" -> isWalls, "blanket" -> isBlanket, "screen" -> isScreen, "water" -> isWater, "mathematician" -> isMathematician, "person" -> isPerson, "window" -> isWindow, "book" -> isBook)

  val d_rel2: Map[KeyPhrase, PredBin] = Map("sees" -> doesSee)

  val l_ents = Set("the blanket", "the water", "the person", "the walls", "the thick red paperback", "the dirty shirt", "the towel", "the window", "the spider", "the ant", "the kettle", "the empty glass", "the books")
  var l_det = Set("a", "an", "the")
  var l_verbs = Set("lives", "glows")
  var l_adjs = Set("wet", "dead", "alive", "empty", "blue", "dirty", "red", "thick")
  var l_ns = Set("dish", "animal", "cup", "mathematician", "walls", "person", "screen", "water", "blanket", "window", "book")
  var l_aux = Set("is", "are")
  var l_conj = Set("and","or")
  var l_quants = Set("some", "all", "no")
  var a_lex: Map[KeyPhrase, Set[KeyPhrase]] = Map("Entity" -> d_entities.keySet, "Intransitive Verb" -> l_verbs, "Transitive Verb" -> d_rel2.keySet, "Auxiliary Verb" -> l_aux, "Conjunction" -> l_conj, "Adjective" -> l_adjs, "Noun" -> l_ns, "Determiner" -> l_det, "Quantifiers" -> l_quants)

  val discRepresentation = new Box(Seq(), Seq())

  val triggers: Map[String, Monologue] = Map("the water is wet" -> Monologue.intro, "the blanket is dirty" -> Monologue.color, "an animal lives" -> Monologue.metaphor, "the animal lives" -> Monologue.metaphor, "the mathematician is dead" -> Monologue.thx, "the screen glows" -> Monologue.glows, "a screen glows" -> Monologue.glows, "the window glows" -> Monologue.window, "the thick red paperback lives" -> Monologue.itself, "the thick red paperback glows" -> Monologue.delaney, "the screen is the window" -> Monologue.windowscreen)

  val d_model: DiscoWorld = new DiscoWorld(d_entities, d_rel1, d_rel2, a_lex,"This room", triggers)
}