name := """latex2svg"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  anorm,
  cache,
  "batik" % "batik-svggen" % "1.6-1",
  "batik" % "batik-1.5-fop" % "0.20-5"
)
