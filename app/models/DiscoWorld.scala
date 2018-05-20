package models

import scala.collection.immutable._

class DiscoWorld(entities: Map[KeyPhrase, Entity],
                 relations1: Map[KeyPhrase, PredSing],
                 relations2: Map[KeyPhrase, Entity => Entity => Boolean],
                 val lexicon: Map[KeyPhrase, Set[KeyPhrase]] = Map(),
                 val name: String = "unknown world",
                 val m_triggers: Map[String, Monologue] = Map(),
                 val m_syntax: Map[(String, String), String] = shit_syntax
                ) extends World(entities, relations1, relations2){

  def shitParse(parsed: List[Map[String, String]]): String = {
    val full_phrase: String = parsed.foldLeft("")((s, p) => s + p("phrase") + " ").toLowerCase.trim
    val failure_msg: String = "This language does not determine whether or not <strong>" + full_phrase + "</strong>. You must be speaking some other language, if you are speaking language at all. "
    val key_error_msg: String = "It is not the case that <strong>" + full_phrase + "</strong>. "
    val its_true_msg: String = "<strong>" + full_phrase.capitalize + "</strong>. "
    val its_false_msg: String = "It is not the case that <strong>" + full_phrase + "</strong>. "

    parsed match {
      case Nil => "There is nothing here. "
      case head :: Nil => head("cat") match {
          case "Sentence" => "It seems we're done here, too late for meaning"
          case _ => failure_msg
        }
      case fst :: snd :: Nil => {
        (fst("cat"), snd("cat")) match {
          case ("Entity", "Intransitive Verb") =>
            if ((entities.keySet contains fst("phrase")) &&
              (relations1.keySet contains snd("phrase")))
            {
              if(relations1(snd("phrase"))(entities(fst("phrase")))) { its_true_msg }
              else { its_false_msg }
            }
            else { key_error_msg }
          case _ => failure_msg
        }
      }
      case fst :: snd :: thd :: Nil =>
        (fst("cat"), snd("cat"), thd("cat")) match {
          case ("Entity", "Auxiliary Verb", "Adjective") =>
            if((entities.keySet contains fst("phrase")) &&
              (Set("is","are") contains snd("phrase")) &&
              (relations1.keySet contains thd("phrase")))
            {
              if(relations1(thd("phrase"))(entities(fst("phrase")))) { its_true_msg }
              else { its_false_msg }
            }
            else { key_error_msg }
          case _ => failure_msg
          }
      case _ => failure_msg
    }
  }

  def evalSent(parsed: List[Map[String, String]]): String = {
    val full_phrase: String = parsed.foldLeft("")((s, p) => s + p("phrase") + " ").toLowerCase.trim
    val response: String = if(m_triggers.keySet contains full_phrase) {
      shitParse(parsed) ++ "<br><br>" ++ m_triggers(full_phrase).text
    }
    else {
      shitParse(parsed)
    }
      response + "</br></br>"
  }

}
