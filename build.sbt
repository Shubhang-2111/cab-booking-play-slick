name := """cab-booking-play"""
organization := "com.shubhang"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.postgresql" % "postgresql" % "42.3.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "com.github.tminglei" %% "slick-pg" % "0.20.3"
)
libraryDependencies +="org.mockito" % "mockito-core" % "1.9.5" % "test"





// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.shubhang.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.shubhang.binders._"
