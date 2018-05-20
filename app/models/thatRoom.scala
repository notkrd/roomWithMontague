package models

import scala.collection.immutable.{Seq, Set}

object thatRoom{
  val d_entities: Map[KeyPhrase, Entity] = Map("the semanticist" -> "MONTAGUE", "the mathematician" -> "MONTAGUE", "the bath towel" -> "TOWEL", "the ant" -> "ANT", "the water" -> "WATER", "the soap dish" -> "SOAP_DISH", "the wallet" -> "WALLET")

  val isAlive: PredSing = Set("ANT")
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
  var a_lex: Map[KeyPhrase, Set[KeyPhrase]] = Map("Entity" -> d_entities.keySet, "Intransitive Verb" -> l_verbs, "Transitive Verb" -> d_rel2.keySet, "Auxiliary Verb" -> l_aux_verbs, "Adjective" -> l_adjs, "Noun" -> l_ns, "Determiner" -> l_dets)

  val discRepresentation = new Box(Seq(), Seq())

  val triggers: Map[String, Monologue] = Map("the mathematician is dead" -> Monologue.montague, "the semanticist is dead" -> Monologue.montague_sem)

  val d_model: DiscoWorld = new DiscoWorld(d_entities, d_rel1, d_rel2, a_lex,"That room", triggers)
}