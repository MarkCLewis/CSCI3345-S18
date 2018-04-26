package spa

import org.scalajs.dom
import org.scalajs.dom._
import dom.document
import scala.scalajs.js.annotation.JSExportTopLevel
import org.querki.jquery._
import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js._

@js.native
trait MyShape extends js.Object

@js.native
class Circle(
  var x: Int,
  var y: Int,
  var radius: Int) extends js.Object with MyShape

@js.native
class Rectangle(
  var x: Int,
  var y: Int,
  var width: Int,
  var height: Int) extends js.Object with MyShape

object SPAMain {

  def main(): Unit = {
    val originalContent = $("#mainContent").text
    anotherMethod()
    document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    $("body").append("<p>Added via jQuery in Scala.js.</p>")
    $("#button0").click(() => restoreContents(originalContent))
    $("#button1").click(() => requestButton1Contents())
    $("#button2").click(() => requestButton2Contents())
    $("#sendCircle").click(() => sendCircle())
    $("#showCanvas").click(() => canvasSetup())
    openWebSocket()
  }

  def anotherMethod(): Unit = {
    println("Printing stuff.")
  }

  def restoreContents(str: String): Unit = {
    $("#mainContent").text(str)
  }

  def route(n: Int): String = {
    $("#route" + n).value().toString
  }

  def requestButton1Contents(): Unit = {
    println("Button 1 clicked.")
    $.get(route(1), success = { (obj, _, _) =>
      println(JSON.stringify(obj))
      val circles = obj.asInstanceOf[Array[MyShape]]
      if(circles.head.hasOwnProperty("radius")) {
        val circle = circles.head.asInstanceOf[Circle]
        println(circle.x, circle.y, circle.radius)
        $("#mainContent").text(s"""Got a circle as JSON at (${circle.x}, ${circle.y}) with radius ${circle.radius}""")
      } else {
        println("Not a circle")
      }
    })
  }

  def requestButton2Contents(): Unit = {
    $.get(route(2), success = { (obj, _, _) =>
      println(obj)
      $("#mainContent").text(obj.toString)
    })
  }

  def sendCircle(): Unit = {
    val x = $("#xValue").value().toString.toInt
    val y = $("#yValue").value().toString.toInt
    val rad = $("#radValue").value().toString.toInt
    println(s"$x $y $rad")
    val c = js.Dynamic.literal(x = x, y = y, radius = rad)
    val route = $("#setCircleRoute").value().toString
    println("Sending " + JSON.stringify(c) + " to " + route)
    $.ajax(route, js.Dynamic.literal(data = JSON.stringify(c), contentType = "application/json", `type` = "POST", dataType = "json").asInstanceOf[JQueryAjaxSettings])
  }

  def canvasSetup(): Unit = {
    val route = $("#canvasRoute").value().toString
    println("Canvas = " + route)
    $("#mainContent").load(route, "", { (_: dom.Element, _: String, _: String, _: JQueryXHR) =>
      {
        val canvas = $("#spacanvas")(0).asInstanceOf[html.Canvas]
        val gc = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
        gc.fillRect(100, 100, 20, 30)
      }
    }: js.ThisFunction3[dom.Element, String, String, JQueryXHR, scala.Any])
  }

  def openWebSocket(): Unit = {
    val wsRoute = $("#wsRoute").value().toString
    val socket = new WebSocket(wsRoute)
    socket.addEventListener("open", (event: Event) => println("Socket open"))
    socket.addEventListener("message", (event: MessageEvent) => {
      val newP = $(s"<p>${event.data}</p>")
      $("#messages").prepend(newP)
    })

    val textInput = $("#input")
    textInput.keyup({ (event: JQueryEventObject) => {
      if (event.which == 13) {
        socket.send(textInput.value.toString)
        textInput.value("")
      }
    }})
  }
}
