package controllers

import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import javax.inject._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TempController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  val td = new models.TempData("data/SanAntonioTemps.csv")

  def welcome = Action { request =>
    println(request.session)
    Ok(views.html.tempWelcome("<b>HI!</b>")).withSession("userid" -> "Mark")
  }

  def tempTable() = Action {
    val month = 2
    val year = 1962
    Ok("Requested table " + month + "/" + year)
  }

  def tempPlot(month: Int, year: Int) = Action.async {
    Future {
      Ok("Requested plot " + month + "/" + year)
    }
  }

}