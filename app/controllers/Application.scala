package controllers

import play.api.libs.iteratee.Enumerator
import play.api.mvc._
 import play.api.Play.current

case class ConfigurationException(message: String) extends Exception(message)

object Application extends Controller {

  val tokenFromConf : String = current.configuration.getString("token") match {
    case Some(x) => x
    case None => throw new ConfigurationException("missing configuration parameter: token")
  }

  def index = Action {
    Ok(views.html.index("LaTeX to SVG server"))
  }
  def process(token: String, latex: String, size: Int, bg: String, fg: String) = Action {
    request => {
      if (token ==  tokenFromConf) {
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
      } else {
        Result(
          header = ResponseHeader(
            200,
            Map(
              CONTENT_TYPE -> "text/plain",
              "BystroTeX-error" -> "CSRF"
            )),
          body = Enumerator("CSRF detected".getBytes)
        )
      }
    }
  }

}