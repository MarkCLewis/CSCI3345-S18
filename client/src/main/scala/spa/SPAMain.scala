package spa

import org.scalajs.dom

object SPAMain {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }
}
