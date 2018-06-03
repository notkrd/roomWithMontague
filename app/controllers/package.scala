import models.{DiscoWorld, Phrase, PhrasesLexicon, TaggedWord, thatRoom, thisRoom}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._


import scala.collection.immutable.{Map, Set}

package object controllers {

  // Accessing world data

  def rooms_known: Map[String, DiscoWorld] = Map(thisRoom.d_model.name -> thisRoom.d_model, thatRoom.d_model.name -> thatRoom.d_model)


  //  Json serializers

  def make_word(cat: String, phr: String): TaggedWord = (cat, phr)

  val readsWord: Reads[TaggedWord] = (
    (JsPath \ "cat").read[String] and
      (JsPath \ "phrase").read[String]
    )(make_word _)

  val writesWord: Writes[TaggedWord] = (
    (JsPath \ "cat").write[String] and
      (JsPath \ "phrase").write[String]
    )(identity[TaggedWord] _)

  implicit val formatWord: Format[TaggedWord] = Format(readsWord, writesWord)

  implicit val formatPhrase: Format[Phrase] = Format(Reads.list(formatWord.reads), Writes.list(formatWord.writes))

  implicit val formatCat: Format[Set[Phrase]] = Format(Reads.set(formatPhrase.reads), Writes.set(formatPhrase.writes))

  implicit val formatLex: Format[PhrasesLexicon] = Format(Reads.map(formatCat), Writes.map(formatCat))

}
