name := """latex2svg"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  anorm,
  cache,
  "org.freehep" % "freehep-graphicsio" % "2.4",
  "org.freehep" % "freehep-graphicsio-svg" % "2.4",
  "org.freehep" % "freehep-graphics2d" % "2.4",
  "batik" % "batik-svggen" % "1.6-1",
  "batik" % "batik-1.5-fop" % "0.20-5",
  "org.jbibtex" % "jbibtex" % "1.0.15"
)

