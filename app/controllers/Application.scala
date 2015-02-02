package controllers

import play.api.libs.iteratee.Enumerator
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def process(latex: String, size: Int, bg: String, fg: String) = Action {
    request => {
      val svg = Convert.toSVG(latex, size, bg, fg, true)
      if (svg.badLatex) Result(
        header = ResponseHeader(200,
          Map(
            CONTENT_TYPE -> "text/plain",
            "BystroTeX-error" -> "latex"
          )),
        body = Enumerator(svg.errorMessage.getBytes())
      ) else if (svg.generalError) Result(
        header = ResponseHeader(200,
          Map(
            CONTENT_TYPE -> "text/plain",
            "BystroTeX-error" -> "general"
          )),
        body = Enumerator(svg.errorMessage.getBytes())
      ) else
      Result(
        header = ResponseHeader(200,
          Map(
            CONTENT_TYPE -> "text/plain",
            "BystroTeX-depth" -> svg.depth.toString,
            "BystroTeX-height" -> svg.height.toString
          )),
        body = Enumerator(svg.svg.getBytes())
      )
    }
  }

}