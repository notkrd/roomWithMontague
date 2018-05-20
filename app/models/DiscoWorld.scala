package models

import scala.collection.immutable._

class DiscoWorld(entities: Map[KeyPhrase, Entity],
                 relations1: Map[KeyPhrase, PredSing],
                 relations2: Map[KeyPhrase, Entity => Entity => Boolean],
                 val lexicon: Map[KeyPhrase, Set[KeyPhrase]] = Map(),
                 val name: String = "unknown world",
                 val m_triggers: Map[String, Monologue] = Map(),
                 val cat_succs: Map[String, Set[String]] = Map()
                ) extends World(entities, relations1, relations2){

  def evalSent(parsed: List[Map[String, String]]): String = {
    val full_phrase: String = (parsed.foldLeft("")((s, p) => s + p("phrase") + " ")).toLowerCase.trim
    val response: String = if(m_triggers.keySet contains full_phrase) {
      "<strong>" ++ formatStr(full_phrase) ++ "</strong>" ++ m_triggers(full_phrase).text
    }
    else {
      "This language does not determine whether or not it is the case that <strong>" ++ full_phrase ++ "</strong>. You must be speaking some other language, if you are speaking language at all."
    }
      return response + "</br></br>"
  }

}
