/**
  * Created by andrei on 14/08/16.
  */
import play.api.Play.current

package object controllers {
//  val tokenFromConf : String = current.configuration.getString("token") match {
//    case Some(x) => x
//    case None => throw new ConfigurationException("missing configuration parameter: token")
//  }
  val ip : String = current.configuration.getString("http.address") match {
    case Some(x) => x
    case None => throw new ConfigurationException("missing configuration parameter:\n" +
      " http.address")
  }
  val port : String = current.configuration.getString("http.port")  match {
    case Some(x) => x
    case None => throw new ConfigurationException("missing configuration parameter:\n" +
      " http.port")
  }
  val bystroServerConfFile : String = current.configuration.getString("bystroFile") match {
    case Some(x) => x
    case None => throw new ConfigurationException("missing configuration parameter: \n" +
      "bystroFile \n" +
      "which should be the path where to put the configuration file for BystroTeX")
  }

  // http://grokbase.com/t/gg/play-framework/146s1cwcp1/secure-random-number-generation-in-playframework
  val random = {
     val r = new java.security.SecureRandom()
     // NIST SP800-90A recommends a seed length of 440 bits (i.e. 55 bytes)
     r.setSeed(r.generateSeed(55))
     r
   }
   /**
    * Return a URL safe base64 string, which may be larger than numBytes.
    */
   def nextBase64String(numBytes:Int):String = {
     val bytes = new Array[Byte](numBytes)
     random.nextBytes(bytes)
     val encodedBytes = java.util.Base64.getUrlEncoder.encode(bytes)
     new String(encodedBytes, "UTF-8")
   }
  val tokenRandom = nextBase64String(128)
  val bystroConf : scala.xml.Node =
    <server><host>{ip}</host><port>{port}</port><path>svg</path><bibpath>bibtex</bibpath><token>{tokenRandom}</token></server>
  def writeBystroConf = scala.xml.XML.save(bystroServerConfFile, bystroConf, "UTF-8", true)

}
