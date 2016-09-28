package controllers

import java.io.{File, FileReader}
import scala.collection.JavaConversions._
import org.jbibtex.{BibTeXDatabase, BibTeXEntry, BibTeXParser, Key}
import play.api.Play.current
import play.api.libs.iteratee.Enumerator
import play.api.mvc._

/**
  * Created by andrei on 14/08/16.
  */
object Bib extends Controller {
  val bibFile: Option[String] = current.configuration.getString("bibfile")

  def getBtDb(x: String): BibTeXDatabase = {
    val fr: FileReader = new FileReader(new File(x))
    val btParser: BibTeXParser = new BibTeXParser()
    btParser.parse(fr)
  }

  val oBtDb: Option[BibTeXDatabase] = bibFile.map(getBtDb)

  def sendXML(token: String, k: String) = Action {
    request => {
      val hmap = request.headers.toMap
      if (!hmap.keys.toList.contains("BystroTeX")) {
        Result(
          header = ResponseHeader(
            403,
            Map(
              CONTENT_TYPE -> "text/plain",
              "BystroTeX-error" -> "foreign origin detected"
            )),
          body = Enumerator("foreign origin detected".getBytes)
        )
      } else if (token == tokenRandom) {
        oBtDb match {
          case Some(x: BibTeXDatabase) => {
            val entry: BibTeXEntry = x.resolveEntry(new Key(k))
            val fields = entry.getFields
            val fsset: Set[Key] = fields.keySet() toSet
            val fs: List[(Key, String)] = for (i <- fsset.toList) yield { i -> fields.get(i).toUserString }
            val marshalled = <bibentry>{for ((a,b) <- fs) yield <v key={a.getValue}>{b}</v>}</bibentry>
            val printer = new scala.xml.PrettyPrinter(80, 2)
            Result(
              header = ResponseHeader(200,
                Map(
                  CONTENT_TYPE -> "text/plain"
                )),
              body = Enumerator(printer.format(marshalled).getBytes())
            )
          }
          case None => {
            Result(
              header = ResponseHeader(200,
                Map(
                  CONTENT_TYPE -> "text/plain" ,
                  "BystroTeX-error" -> "missing configuration parameter: bibfile"
                )),
              body = Enumerator("Error".getBytes())
            )
          }
        }
      } else {
        Result(
          header = ResponseHeader(
            403,
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


