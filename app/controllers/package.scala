/**
  * Created by andrei on 14/08/16.
  */
import play.api.Play.current

package object controllers {
    val tokenFromConf : String = current.configuration.getString("token") match {
    case Some(x) => x
    case None => throw new ConfigurationException("missing configuration parameter: token")
  }
}
