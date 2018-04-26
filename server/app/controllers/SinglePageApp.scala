package controllers

import javax.inject._
import java.util.concurrent.atomic.AtomicInteger
import play.api.mvc._
import play.api.libs.json.Writes
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.JsPath
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import java.util.concurrent.atomic.AtomicReference

@Singleton
class SinglePageApp @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  private val cnt = new AtomicInteger(0)
  private val shapes = new AtomicReference(List[MyShape](Rectangle(1, 2, 3, 4), Circle(4, 5, 6)))

  sealed trait MyShape
  case class Circle(x: Int, y: Int, r: Int) extends MyShape
  case class Rectangle(x: Int, y: Int, width: Int, height: Int) extends MyShape

  implicit val circleWrites = new Writes[Circle] {
    def writes(circle: Circle) = Json.obj(
      "x" -> circle.x,
      "y" -> circle.y,
      "radius" -> circle.r)
  }

  val circleReads: Reads[MyShape] = (
    (JsPath \ "x").read[Int] and
    (JsPath \ "y").read[Int] and
    (JsPath \ "radius").read[Int])(Circle.apply _)

  implicit val rectangleWrites = new Writes[Rectangle] {
    def writes(rect: Rectangle) = Json.obj(
      "x" -> rect.x,
      "y" -> rect.y,
      "width" -> rect.width,
      "height" -> rect.height)
  }

  val rectangleReads: Reads[MyShape] = (
    (JsPath \ "x").read[Int] and
    (JsPath \ "y").read[Int] and
    (JsPath \ "width").read[Int] and
    (JsPath \ "height").read[Int])(Rectangle.apply _)

  implicit val shapeWrites = new Writes[MyShape] {
    def writes(shape: MyShape) = shape match {
      case c: Circle => circleWrites.writes(c)
      case r: Rectangle => rectangleWrites.writes(r)
    }

  }

  implicit val shapeReads: Reads[MyShape] = circleReads.or(rectangleReads)

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
    Ok(Json.toJson(shapes.get))
  }

  def button2Call = Action { implicit request =>
    Ok(s"This is plain text. Count = ${cnt.incrementAndGet()}. Message is ${spa.SharedMessages.itWorks}")
  }

  def setCircle = Action(parse.json) { implicit request =>
    println(request.body)
    val c = request.body.validate[MyShape].get
    shapes.set(c :: shapes.get)
    Ok("Circle set to " + c)
  }

  def jscanvas = Action { implicit request =>
    Ok(<canvas width="800" height="600" id="spacanvas"/>)
    // If you had something more significant, you could use a view like the following.
    //    Ok(views.html.canvasElement())
  }
}
