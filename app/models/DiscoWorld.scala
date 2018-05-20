package models

import scala.collection.immutable._

class DiscoWorld(entities: Map[KeyPhrase, Entity],
                 relations1: Map[KeyPhrase, PredSing],
                 relations2: Map[KeyPhrase, Entity => Entity => Boolean],
                 val lexicon: Map[KeyPhrase, Set[KeyPhrase]] = Map(),
                 val name: String = "unknown world",
                 val m_triggers: Map[String, Monologue] = Map(),
                 val m_seqs: Map[String, Set[String]] = Map()
                ) extends World(entities, relations1, relations2){

}
