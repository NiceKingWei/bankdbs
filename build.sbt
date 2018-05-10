name := """bankdbs"""
organization := "pers.nicekingwei"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.11"
//libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
//libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "pers.nicekingwei.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "pers.nicekingwei.binders._"
