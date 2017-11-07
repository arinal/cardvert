lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.scout24",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "cardvert",
    libraryDependencies ++= Seq(
      "com.softwaremill.macwire" %% "macros"    % "2.3.0",

      "joda-time"          %  "joda-time"       % "2.9.1",
      "io.circe"           %% "circe-core"      % "0.8.0",
      "io.circe"           %% "circe-generic"   % "0.8.0",
      "io.circe"           %% "circe-parser"    % "0.8.0",
      "ch.qos.logback"     %  "logback-classic" % "1.1.2",

      "com.typesafe.akka"  %% "akka-http"       % "10.0.10",

      "org.scalatest"      %% "scalatest"       % "3.0.3" % Test
    )
  )
