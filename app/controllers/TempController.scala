package controllers

import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import javax.inject._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

case class TempRange(startMonth: Int, startYear: Int, endMonth: Int, endYear: Int)

@Singleton
class TempController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  val td = new models.TempData("data/SanAntonioTemps.csv")

  val tempRangeForm = Form(mapping(
    "startMonth" -> number(min = 1, max = 12),
    "startYear" -> number(min = 1946, max = 2014),
    "endMonth" -> number(min = 1, max = 12),
    "endYear" -> number(min = 1946, max = 2014))(TempRange.apply)(TempRange.unapply))

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

  def tempPlot = Action.async { implicit request =>
    Future {
      tempRangeForm.bindFromRequest().fold(
          formWithErrors => BadRequest,
          tempRange => 
            Ok("Requested plot " + tempRange))
    }
  }

}