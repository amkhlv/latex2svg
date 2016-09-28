import controllers.{Application => _, _}
import play.api._

/**
  * Created by andrei on 27/09/16.
  */
object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("latex2svg: starting")
    writeBystroConf
  }

  override def onStop(app: Application) {
    Logger.info("latex2svg: ByeBye...")
  }

}


