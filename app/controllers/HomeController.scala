package controllers

import javax.inject._
import play.api.mvc._
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
    Ok(views.html.roomview("Room With Montague", "the water is wet", thisRoom.d_model, style="scala"))
  }


  def showThatRoom = Action {
    Ok(views.html.roomview("Room With Montague", "the mathematician is dead", thatRoom.d_model, style="scala"))
  }

  def getMonologue = Action { request  =>
    val request_params = request.queryString.map { case (k,v) => k -> v.mkString }
    if(request_params.keySet contains "id") {
      Ok(Monologue.monologues(request_params("id")).text)
    }
    else{
      BadRequest
    }
  }

  def formatStr(str: String): String = {
    str.trim.capitalize ++ ".<br><br>"
  }

  def assert = Action { request =>
    val request_params = request.queryString.map { case (k,v) => k -> v.mkString }
    if(request_params.keySet contains "utterance") {
      var response = formatStr(request_params("utterance"))
      if(request_params("utterance").toLowerCase.trim == "the water is wet") {
        response += Monologue.monologues("intro").text
      }
      else if(request_params("utterance").toLowerCase == "the mathematician is dead") {
        response += Monologue.monologues("montague").text
      }
      else {
        response += formatStr("unknown")
      }
      Ok(response)
    }
    else{
      BadRequest
    }

  }
}
