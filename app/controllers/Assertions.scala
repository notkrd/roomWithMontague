package controllers

import javax.inject._
import play.api.mvc._
import models.DiscoWorld
import play.api.libs.json._
import play.api.mvc.Request

import scala.collection.immutable.{List, Map, Set}
import models._

class Assertions @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def rooms_known: Map[String, DiscoWorld] = Set(thisRoom.d_model, thatRoom.d_model).foldLeft(Map.empty[String, DiscoWorld])((m, w) => m + (w.name -> w))

  def assert(wname: String): Action[JsValue] = Action(parse.json) { request: Request[JsValue] =>
    if (rooms_known.keySet contains wname) {
      val the_world: DiscoWorld = rooms_known(wname)
      val parsedSent: JsResult[List[Map[String, String]]] = request.body.validate[List[Map[String, String]]]((JsPath \ "parsed").read[List[Map[String, String]]])

      parsedSent match {
        case s: JsSuccess[List[Map[String, String]]] => Ok(the_world.evalSent(s.get))
        case e: JsError => Ok("Errors: " + JsError.toJson(e).toString())
      }
    }
    else {
      Ok("You are somewhere else, unspeakable.")
    }
  }
}
