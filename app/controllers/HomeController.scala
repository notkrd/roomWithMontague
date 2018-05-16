package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json.{Json, JsValue}


import scala.collection.immutable.{Map, Set, Seq}

import models.{Monologue, thisRoom, thatRoom, World}

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
  def showThisRoom = Action {
    Ok(views.html.roomview("Room With Montague", thisRoom.d_model, style="scala"))
  }


  def showThatRoom = Action {
    val mathematician_parsed: Seq[Map[String, String]] = Seq(Map("phrase" -> "the mathematician", "cat" -> "NP"), Map("phrase" -> "is dead", "cat" -> "VP"))
    Ok(views.html.roomview("Room With Montague", thatRoom.d_model, style="scala"))
  }

  def formatStr(str: String): String = {
    str.trim.capitalize ++ ". "
  }

  def getMonologue = Action { request  =>
    val request_params: Map[String, String] = request.queryString.map { case (k,v) => k -> v.mkString }
    if(request_params.keySet contains "id") {
      Ok(Monologue.monologues(request_params("id")).text)
    }
    else{
      BadRequest
    }
  }

  def assert = Action { request =>
    val request_params: Map[String, String] = request.queryString.map { case (k,v) => k -> v.mkString }
    if(request_params.keySet contains "utterance") {
      val utterance_in: String = request_params("utterance").toLowerCase.trim
      val response: String = if(Monologue.triggers.keySet contains utterance_in) {
        "<strong>" ++ formatStr(utterance_in) ++ "</strong>" ++ Monologue.triggers(utterance_in).text
      }
      else {
        formatStr("this language does not determine whether or not it is the case that " ++ "<strong>" ++ utterance_in ++ "</strong>. You must be speaking some other language, if you are speaking language at all")
      }
      Ok(response ++ "<br><br>")
    }
    else{
      BadRequest
    }
  }
}
