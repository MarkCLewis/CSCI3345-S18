package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json.Writes
import play.api.libs.json.Json
import java.util.concurrent.atomic.AtomicInteger

@Singleton
class SinglePageApp @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  private val cnt = new AtomicInteger(0)
  
  case class Circle(x: Int, y: Int, r: Int)

  implicit val circleWrites = new Writes[Circle] {
    def writes(circle: Circle) = Json.obj(
      "x" -> circle.x,
      "y" -> circle.y,
      "radius" -> circle.r)
  }

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action { implicit request =>
    Ok(views.html.singlePageApp())
  }

  def button1Call = Action { implicit request =>
    val circle = new Circle(4, 5, 7)
    Ok(Json.toJson(circle))
  }

  def button2Call = Action { implicit request =>
    Ok(s"This is plain text. Count = ${cnt.incrementAndGet()}. Message is ${spa.SharedMessages.itWorks}")
  }

}
