package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.routing._

import scala.collection.immutable.{Map, Set, Seq}

import models.{Monologue, thisRoom, thatRoom, DiscoWorld}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def rooms_known: Map[String, DiscoWorld] = Set(thisRoom.d_model, thatRoom.d_model).foldLeft(Map.empty[String, DiscoWorld])((m, w) => m + (w.name -> w))

  def showThisRoom = Action { implicit request =>
    Ok(views.html.roomview("Room With Montague", rooms_known("This room"), style="scala"))
  }


  def showThatRoom = Action { implicit request =>
    Ok(views.html.roomview("Room With Montague", rooms_known("That room"), style="scala"))
  }

// Parsing Json. AArrrgaaa

// Handling requests

  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.HomeController.assert
      )
    ).as("text/javascript")
  }


  def assert(wname: String) = Action(parse.json) { request: Request[JsValue] =>
    if (rooms_known.keySet contains wname) {
      val the_world: DiscoWorld = rooms_known(wname)
      val parsedSent: JsResult[List[Map[String, String]]] = request.body.validate[List[Map[String, String]]]((JsPath \ "parsed").read[List[Map[String, String]]])

      parsedSent match {
        case s: JsSuccess[List[Map[String, String]]] => {
          Ok(the_world.evalSent(s.get))
        }
        case e: JsError => Ok("Errors: " + JsError.toJson(e).toString())
      }
    }
    else {
      Ok("You are somewhere else, unspeakable.")
    }
  }

//  def assert = Action { implicit request: Request[AnyContent] =>
//
//    val body: AnyContent = request.body
//    val jsonBody: Option[JsValue] = body.asJson
//
//    // Expecting json body
//    val request_params: Map[String, String] = request.queryString.map { case (k,v) => k -> v.mkString }
//    if(Set("utterance", "world", "parsed[0][phrase]") subsetOf request_params.keySet) {
//      val utterance_in: String = request_params("utterance").toLowerCase.trim
//      val response: String = if(Monologue.triggers.keySet contains utterance_in) {
//        "<strong>" ++ formatStr(utterance_in) ++ "</strong>" ++ Monologue.triggers(utterance_in).text
//      }
//      else {
//        formatStr("this language does not determine whether or not it is the case that " ++ "<strong>" ++ utterance_in ++ "</strong>. You must be speaking some other language, if you are speaking language at all")
//      }
//      Ok(response ++ "<br><br>")
//    }
//    else{
//      Ok("Invalid utterance. Please try to speak again. Your headers are" ++ request_params.keySet.mkString(" "))
//    }
//  }
}
