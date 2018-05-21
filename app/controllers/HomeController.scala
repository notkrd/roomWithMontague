package controllers

import javax.inject._
import play.api.mvc._
import play.api.routing._

import scala.collection.immutable._

import models._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page about a couple rooms.
   * Started out as an intro Play project template.
   * Cobbled with tape and math and tea and stackexchange into something about compositional semantics
   */
  def rooms_known: Map[String, DiscoWorld] = Set(thisRoom.d_model, thatRoom.d_model).foldLeft(Map.empty[String, DiscoWorld])((m, w) => m + (w.name -> w))

  def showThisRoom = Action { implicit request =>
    Ok(views.html.roomview("Room With Montague", rooms_known("This room")))
  }


  def showThatRoom = Action { implicit request =>
    Ok(views.html.roomview("Room With Montague", rooms_known("That room")))
  }

// Parsing Json. AArrrgaaa

// Handling requests

  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.Assertions.assert,
        routes.javascript.Assertions.compose
      )
    ).as("text/javascript")
  }
}
