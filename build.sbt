lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.scout24",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "cardvert",
    libraryDependencies ++= Seq(
      "joda-time"     %  "joda-time" % "2.9.1",
      "org.scalatest" %% "scalatest" % "3.0.3" % Test
    )
  )
