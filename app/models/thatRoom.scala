package models

import scala.collection.immutable.{Seq, Set}

object thatRoom{
  val d_entities: Map[KeyPhrase, Entity] = Map("the mathematician" -> "MONTAGUE", "the successful real estate investor" -> "MONTAGUE", "the semanticist" -> "MONTAGUE", "the organist" -> "MONTAGUE", "the bath towel" -> "TOWEL", "the ant" -> "ANT", "the water" -> "WATER", "the soap dish" -> "SOAP_DISH", "the wallet" -> "WALLET", "the floor" -> "FLOOR", "the walls" -> "WALLS")

  val isAlive: PredSing = Set("ANT")
  val isWet: PredSing = Set("MONTAGUE", "WATER")
  val isTowel: PredSing = Set("TOWEL")
  val isMathemetician: PredSing = Set("MONTAGUE")
  val isSemanticist: PredSing = Set("MONTAGUE")
  val isPhilosopher: PredSing = Set("MONTAGUE")
  val isRealEstateInvestor: PredSing = Set("MONTAGUE")
  val isOrganist: PredSing = Set("MONTAGUE")
  val isSolid: PredSing = Set()
  val isDead: PredSing = Set("MONTAGUE")
  val isWater: PredSing = Set("WATER")
  val isFull: PredSing = Set("WALLET", "MONTAGUE", "SOAP_DISH")

  val seeTuples: Set[Tuple2[Entity,Entity]] = (for {a_thing <- d_entities.values} yield ("AUTHOR", a_thing)).toSet ++ Set(("ANT","MONTAGUE"), ("ANT", "TOWEL"), ("ANT", "FLOOR"))
  val doesSee: PredBin = tuplesToPredBin(seeTuples)
  val doesCover: PredBin = tuplesToPredBin(Set(("MONTAGUE", "TOWEL"), ("SOAP_DISH", "WALLET"), ("FLOOR", "WALLS")))

  val d_rel1: Map[KeyPhrase, PredSing] = Map("lives" -> isAlive, "wet" -> isWet, "towel" -> isTowel, "dead" -> isDead, "semanticist" -> isSemanticist, "mathemetician" -> isMathemetician, "philosopher" -> isPhilosopher, "successful real estate investor" -> isRealEstateInvestor, "water" -> isWater, "organist" -> isOrganist, "full" -> isFull)
  val d_rel2: Map[KeyPhrase, PredBin] = Map("see" -> doesSee, "covers" -> doesCover)

  val l_ents: Set[List[(String, String)]] = Set(List(("Entity", "the soap dish")), List(("Entity", "the wallet")))
  var l_verbs = Set("lives")
  var l_adjs = Set("wet", "dead", "full")
  var l_ns = Set("towel", "mathematician", "semanticist", "successful real estate investor", "water", "organist")
  var l_aux = Set("is", "are", "is not")
  var l_dets= Set("a", "an", "the")
  var l_quants: Set[String] = Set("a", "all", "every", "some", "no")
  var l_conj = Set("and", "or")

  var a_lex: Map[String, Set[List[(String, String)]]] = Map("Entity" -> l_ents, wrapPhraseSet("Intransitive Verb")(l_verbs), wrapPhraseSet("Transitive Verb")(d_rel2.keySet), wrapPhraseSet("Auxiliary Verb")(l_aux), wrapPhraseSet("Conjunction")(l_conj), wrapPhraseSet("Adjective")(l_adjs), wrapPhraseSet("Noun")(l_ns), wrapPhraseSet("Determiner")(l_dets), wrapPhraseSet("Quantifier")(l_quants), "Utterance" -> Set[List[(String, String)]]())

  val discRepresentation = new Box(Seq(), Seq())

  val triggers: Map[String, Monologue] = Map("the mathematician is dead" -> Monologue.montague, "the semanticist is dead" -> Monologue.montague_sem, "the organist is dead" -> Monologue.montague_org, "the successful real estate investor is dead" -> Monologue.montague_inv,"the water is wet" -> Monologue.blood, "is wet" -> Monologue.wet,  "towel covers the mathematician" -> Monologue.knots, "the walls see the mathematician" -> Monologue.langs, "the wallet covers the soap dish" -> Monologue.soap, "the soap dish covers the wallet" -> Monologue.flip, "the ant lives" -> Monologue.unknowns, "the water is not wet" -> Monologue.contradiction, "the mathematician is not dead" -> Monologue.contradiction, "the mathematician sees" -> Monologue.absence, "no" -> Monologue.absence, " or " -> Monologue.choices, "the organist" -> Monologue.unspeakables, "wallet lives" -> Monologue.wallet_life, "walled is dead" -> Monologue.wallet_life)

  val d_model: DiscoWorld = new DiscoWorld(d_entities, d_rel1, d_rel2, a_lex,"That room", triggers, epigraph = "<q>Close beside a leafy thicket:-- / On his nose there was a Cricket,-- / In his hat a Railway-Ticket,-- /        (But his shoes were far too tight.)</q> -Edward Lear, <em>Incidents in the Life of My Uncle Arly.</em>")
}