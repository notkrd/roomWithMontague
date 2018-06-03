package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._


import scala.collection.immutable.{List, Map, Set}
import models._

class LexiconController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getLexicon(wname: String) = Action {

    if(rooms_known.keySet contains wname) {
      val this_world: DiscoWorld = rooms_known(wname)
      Ok(formatLex.writes(this_world.lexicon))
    }
    else {
      val this_world: DiscoWorld = rooms_known(thisRoom.d_model.name)
      Ok(formatLex.writes(this_world.lexicon))
    }
  }

}
