package controllers

import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import javax.inject._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TempController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  val td = new models.TempData("data/SanAntonioTemps.csv")

  def welcome = Action { implicit request =>
    println(request.session)
    Ok(views.html.tempWelcome("<b>HI!</b>")).withSession("userid" -> "Mark")
  }

  def tempTable() = Action { implicit request =>
    request.body.asFormUrlEncoded match {
      case Some(formData) =>
        val month = formData("month").head.toInt
        val year = formData("year").head.toInt
        val data = td.getMonth(month, year)
    	  Ok(views.html.tempTable(data, month, year))
      case None =>
        Redirect(routes.TempController.welcome)
    }
  }

  def tempPlot(month: Int, year: Int) = Action.async { implicit request =>
    Future {
      Ok("Requested plot " + month + "/" + year)
    }
  }

}