package controllers

import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import javax.inject._

@Singleton
class TempController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def welcome = Action {
    Ok(views.html.tempWelcome())
  }
}