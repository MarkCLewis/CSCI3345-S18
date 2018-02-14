package controllers

import javax.inject._
import play.api.mvc._
import java.io.ByteArrayOutputStream
import swiftvis2.plotting.renderer.SVGRenderer
import java.io.PrintStream
import swiftvis2.plotting.Plot
import swiftvis2.plotting.Bounds

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  
  def plot = Action {
	  val plot = Plot.scatterPlot(1 to 10, (1 to 10).map(i => i*i), "Plot", "X", "Y")
    
    Ok(SVGRenderer.stringValue(plot, 800, 600)).as("image/svg+xml")
  }
}
