package controllers

import javax.inject._
import play.api.mvc._
import models.DiscoWorld
import play.api.libs.json._
import play.api.mvc.Request
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.collection.immutable.{List, Map, Set}
import models._

class Assertions @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def rooms_known: Map[String, DiscoWorld] = Map(thisRoom.d_model.name -> thisRoom.d_model, thatRoom.d_model.name -> thatRoom.d_model)

  def compose(wname: String) = Action { implicit request =>
    if ((rooms_known.keySet contains wname) && (Set("lcat", "rcat") subsetOf request.queryString.keySet)) {
      val lcat = request.queryString("lcat").mkString
      val rcat = request.queryString("rcat").mkString
      val the_world: DiscoWorld = rooms_known(wname)
      val the_response = if(the_world.m_syntax.keySet contains (lcat, rcat)) {
        val new_cat = the_world.m_syntax((lcat, rcat))
        val new_opts: Set[String] = the_world.possSuccs(new_cat)
        Json.obj("new_cat" -> new_cat, "new_opts" -> new_opts.toList)
      }
      else {
        Json.obj("new_cat" -> "Gibberish", "new_opts" -> List())
      }
      Ok(the_response)
    }
    else { BadRequest("You are somewhere else, unspeakable: " + request.queryString.mkString) }
  }

  def assert(wname: String): Action[JsValue] = Action(parse.json) { implicit request: Request[JsValue] =>
    if (rooms_known.keySet contains wname) {
      val the_world: DiscoWorld = rooms_known(wname)
      val parsedSent: JsResult[List[Map[String, String]]] = request.body.validate[List[Map[String, String]]]((JsPath \ "parsed").read[List[Map[String, String]]])

      parsedSent match {
        case s: JsSuccess[List[Map[String, String]]] => {
          val evaluated: (String, PhrasesLexicon) = the_world.evalSent(s.get)
          val new_phrases_json: JsValue = formatLex.writes(evaluated._2)
          Ok(Json.obj("discourse" -> evaluated._1, "success" -> true, "new_phrases" -> new_phrases_json))
        }
        case e: JsError => Ok(Json.obj("discourse" -> ("Errors: " + JsError.toJson(e).toString()), "success" -> false))
      }
    }
    else {
      Ok(Json.obj("discourse" -> "You are somewhere else, unspeakable.", "success" -> false))
    }
  }
}
