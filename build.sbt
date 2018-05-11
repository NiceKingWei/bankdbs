lazy val databaseUrl = sys.env.getOrElse("BANKDBS_URL", "BANKDBS_URL is not set")
lazy val databaseUser = sys.env.getOrElse("BANKDBS_USER", "BANKDBS_USER is not set")
lazy val databasePassword = sys.env.getOrElse("BANKDBS_PASSWORD", "BANKDBS_PASSWORD is not set")

lazy val root = (project in file(".")).enablePlugins(PlayScala)

name := """bankdbs"""
organization := "pers.nicekingwei"
version := "1.0"
scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"
)
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.34"
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.2.3"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

/*
// uncommment if need code generating
import slick.codegen.SourceCodeGenerator
import slick.{ model => m }
enablePlugins(CodegenPlugin)
sourceGenerators in Compile += slickCodegen

slickCodegenDatabaseUrl := databaseUrl
slickCodegenDatabaseUser := databaseUser
slickCodegenDatabasePassword := databasePassword
slickCodegenDriver := slick.jdbc.MySQLProfile
slickCodegenJdbcDriver := "com.mysql.jdbc.Driver"
slickCodegenOutputPackage := "dao"
slickCodegenExcludedTables := Seq("schema_version","play_evolutions")
slickCodegenOutputDir := (sourceDirectory in Compile).value
*/

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "pers.nicekingwei.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "pers.nicekingwei.binders._"

