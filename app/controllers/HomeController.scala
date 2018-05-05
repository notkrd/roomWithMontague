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
    Ok(views.html.roomview("Room With Montague", "The water is wet.", Monologue.monologues("intro"), thisRoom.d_model, style="scala"))
  }


  def showThatRoom = Action {
    Ok(views.html.roomview("Room With Montague", "The water is wet.", Monologue.monologues("montague"), thatRoom.d_model, style="scala"))
  }

}
