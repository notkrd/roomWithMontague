package models

import scala.collection.immutable.{Seq, Set}

object thatRoom{
  val d_entities: Map[KeyPhrase, Entity] = Map("the semanticist" -> "MONTAGUE", "the towel" -> "TOWEL", "the ant" -> "ANT", "the water" -> "WATER")

  val isAlive: PredSing = Set()
  val isWet: PredSing = Set("MONTAGUE")
  val isTowel: PredSing = Set("TOWEL")
  val isMathemetician: PredSing = Set("Montague")
  val isPhilosopher: PredSing = Set("Montague")
  val isRealEstateInvestor: PredSing = Set("Montague")
  val isSolid: PredSing = Set()
  val isDead: PredSing = Set("MONTAGUE")

  val seeTuples: Set[Tuple2[Entity,Entity]] = (for {a_thing <- d_entities.values} yield ("AUTHOR", a_thing)).toSet + (("ANT","AUTHOR"))
  val doesSee: PredBin = tuplesToPredBin(seeTuples)

  val d_rel1: Map[KeyPhrase, PredSing] = Map("lives" -> isAlive, "wet" -> isWet, "towel" -> isTowel, "dead" -> isDead, "mathemetician" -> isMathemetician, "philosopher" -> isPhilosopher, "succesful real estate investor" -> isRealEstateInvestor)
  val d_rel2: Map[KeyPhrase, PredBin] = Map("sees" -> doesSee)

  var l_verbs = Set("lives")
  var l_adjs = Set("wet", "dead")
  var l_ns = Set("towel", "mathematician", "philosopher", "successful real estate investor")
  var l_aux_verbs = Set("is")
  var l_dets = Set("a(n)", "the")
  var a_lex = Map("Entities" -> d_entities.keySet, "Intransitive Verbs" -> l_verbs, "Transitive Verbs" -> d_rel2.keySet, "Auxiliary Verbs" -> l_aux_verbs, "Adjectives" -> l_adjs, "Nouns" -> l_ns, "Determiners" -> l_dets)

  val discRepresentation = new Box(Seq(), Seq())

  val d_model: World = new World(d_entities, d_rel1, d_rel2, a_lex)
}