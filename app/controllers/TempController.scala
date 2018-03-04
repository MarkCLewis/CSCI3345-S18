package controllers

import javax.inject.Inject
import javax.inject.Singleton
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.number
import play.api.mvc.MessagesAbstractController
import play.api.mvc.MessagesControllerComponents
import swiftvis2.plotting._
import swiftvis2.plotting.renderer.SVGRenderer
import swiftvis2.plotting.styles.ScatterStyle
import swiftvis2.plotting.styles.ScatterStyle.LineData
import swiftvis2.plotting.renderer.Renderer.StrokeData

case class TempRange(startMonth: Int, startYear: Int, endMonth: Int, endYear: Int)

@Singleton
class TempController @Inject() (cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  val td = new models.TempData("data/SanAntonioTemps.csv")

  val tempRangeForm = Form(mapping(
    "startMonth" -> number(min = 1, max = 12),
    "startYear" -> number(min = 1946, max = 2014),
    "endMonth" -> number(min = 1, max = 12),
    "endYear" -> number(min = 1946, max = 2014))(TempRange.apply)(TempRange.unapply))

  def welcome = Action { implicit request =>
    println(request.session)
    Ok(views.html.tempWelcome("<b>HI!</b>", tempRangeForm)).withSession("userid" -> "Mark")
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

  def tempPlotPage() = Action { implicit request =>
    tempRangeForm.bindFromRequest().fold(
      formWithErrors => BadRequest(views.html.tempWelcome("Oops", formWithErrors)),
      tempRange => Ok(views.html.tempPlot(tempRange)))
  }

  def tempPlot(startMonth: Int, startYear: Int, endMonth: Int, endYear: Int) = Action {
    val data = td.getRange(startMonth, startYear, endMonth, endYear)
    val date = data.map(dt => dt.doy / 365.0 + dt.year)
    val plot = Plot.stackedNN(Seq(
      ScatterStyle(date, data.map(_.tmax), symbolWidth = 3, symbolHeight = 3, colors = RedARGB, lines = Some(LineData(1, StrokeData(1, Nil)))),
      ScatterStyle(date, data.map(_.tave), symbolWidth = 3, symbolHeight = 3, colors = BlackARGB, lines = Some(LineData(1, StrokeData(1, Nil)))),
      ScatterStyle(date, data.map(_.tmin), symbolWidth = 3, symbolHeight = 3, colors = GreenARGB, lines = Some(LineData(1, StrokeData(1, Nil))))), "Temperatures", "Year", "Temp (F)").updatedNumericAxis("Main", "x", a => a.copy(tickSpacing = Some(1.0), tickLabelInfo = a.tickLabelInfo.map(_.copy(numberFormat = "%1.0f"))))

    Ok(SVGRenderer.stringValue(plot, 800, 600)).as("image/svg+xml")
  }

  def precipPlot(startMonth: Int, startYear: Int, endMonth: Int, endYear: Int) = Action {
    val data = td.getRange(startMonth, startYear, endMonth, endYear)
    val date = data.map(dt => dt.doy / 365.0 + dt.year)
    val plot = Plot.stackedNN(Seq(
      ScatterStyle(date, data.map(_.precip), symbolWidth = 3, symbolHeight = 3, colors = BlueARGB, lines = Some(LineData(1, StrokeData(1, Nil))))), "Precipitation", "Year", "Precipiation (in)").updatedNumericAxis("Main", "x", a => a.copy(tickSpacing = Some(1.0), tickLabelInfo = a.tickLabelInfo.map(_.copy(numberFormat = "%1.0f"))))

    Ok(SVGRenderer.stringValue(plot, 800, 600)).as("image/svg+xml")
  }
}