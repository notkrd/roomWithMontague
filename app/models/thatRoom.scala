package models

import scala.collection.immutable.{Seq, Set}

object thatRoom{
  val d_entities: Map[KeyPhrase, Entity] = Map("the mathematician" -> "MONTAGUE", "the successful real estate investor" -> "MONTAGUE", "the semanticist" -> "MONTAGUE", "the organist" -> "MONTAGUE", "the bath towel" -> "TOWEL", "the ant" -> "ANT", "the water" -> "WATER", "the soap dish" -> "SOAP_DISH", "the wallet" -> "WALLET", "the floor" -> "FLOOR", "the walls" -> "WALLS")

  val isAlive: PredSing = Set("ANT")
  val isWet: PredSing = Set("MONTAGUE", "WATER")
  val isTowel: PredSing = Set("TOWEL")
  val isMathemetician: PredSing = Set("Montague")
  val isPhilosopher: PredSing = Set("Montague")
  val isRealEstateInvestor: PredSing = Set("Montague")
  val isOrganist: PredSing = Set("Montague")
  val isSolid: PredSing = Set()
  val isDead: PredSing = Set("MONTAGUE")
  val isWater: PredSing = Set("water")

  val seeTuples: Set[Tuple2[Entity,Entity]] = (for {a_thing <- d_entities.values} yield ("AUTHOR", a_thing)).toSet ++ Set(("ANT","MONTAGUE"), ("ANT", "TOWEL"), ("ANT", "FLOOR"))
  val doesSee: PredBin = tuplesToPredBin(seeTuples)
  val doesCover: PredBin = tuplesToPredBin(Set(("TOWEL", "MONTAGUE"), ("WALLET", "SOAP_DISH"), ("WALLS","FLOOR")))

  val d_rel1: Map[KeyPhrase, PredSing] = Map("lives" -> isAlive, "wet" -> isWet, "towel" -> isTowel, "dead" -> isDead, "mathemetician" -> isMathemetician, "philosopher" -> isPhilosopher, "succesful real estate investor" -> isRealEstateInvestor, "water" -> isWater, "orgainst" -> isOrganist)
  val d_rel2: Map[KeyPhrase, PredBin] = Map("sees" -> doesSee, "covers" -> doesCover)

  var l_verbs = Set("lives")
  var l_adjs = Set("wet", "dead")
  var l_ns = Set("towel", "mathematician", "semanticist", "successful real estate investor", "water", "organist")
  var l_aux_verbs = Set("is")
  var l_dets = Set("a(n)", "the")
  var a_lex: Map[KeyPhrase, Set[KeyPhrase]] = Map("Entity" -> (d_entities.keySet -- Set("the semanticist", "the organist", "the successful real estate investor")), "Intransitive Verb" -> l_verbs, "Transitive Verb" -> d_rel2.keySet, "Auxiliary Verb" -> l_aux_verbs, "Adjective" -> l_adjs, "Noun" -> l_ns, "Determiner" -> l_dets)

  val discRepresentation = new Box(Seq(), Seq())

  val triggers: Map[String, Monologue] = Map("the mathematician is dead" -> Monologue.montague, "the semanticist is dead" -> Monologue.montague_sem, "the organist is dead" -> Monologue.montague_org, "the successful real estate investor is dead" -> Monologue.montague_inv,"the water is wet" -> Monologue.blood, "the mathematician is wet" -> Monologue.wet, "the bath towel covers the mathematician" -> Monologue.knots)

  val d_model: DiscoWorld = new DiscoWorld(d_entities, d_rel1, d_rel2, a_lex,"That room", triggers)
}