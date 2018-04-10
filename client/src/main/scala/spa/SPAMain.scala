package spa

import org.scalajs.dom
import dom.document
import scala.scalajs.js.annotation.JSExportTopLevel
import org.querki.jquery._
import scala.scalajs.js.JSON
import scala.scalajs.js

trait Circle extends js.Object {
  var x: Int
  var y: Int
  var radius: Int
}

object SPAMain {

  def main(args: Array[String]): Unit = {
    val originalContent = $("#mainContent").text
    anotherMethod()
    document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    $("body").append("<p>Added via jQuery in Scala.js.</p>")
    $("#button0").click(() => restoreContents(originalContent))
    $("#button1").click(() => requestButton1Contents())
    $("#button2").click(() => requestButton2Contents())
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
      val circle = obj.asInstanceOf[Circle]
      println(circle.x, circle.y, circle.radius)
      $("#mainContent").text(s"""Got a circle as JSON at (${circle.x}, ${circle.y}) with radius ${circle.radius}""")
    })
  }

  def requestButton2Contents(): Unit = {
    $.get(route(2), success = { (obj, _, _) => 
      println(obj)
      $("#mainContent").text(obj.toString)
    })
  }

}
