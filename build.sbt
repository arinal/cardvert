lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Seq(
      organization := "com.scout24",
      scalaVersion := "2.12.3",
      name         := "cardvert",
      version      := "0.1.0-SNAPSHOT"
    ),

    Defaults.itSettings,

    libraryDependencies ++= Seq(
      "com.softwaremill.macwire" %% "macros"    % "2.3.0",

      "joda-time"          %  "joda-time"       % "2.9.1",
      "io.circe"           %% "circe-core"      % "0.8.0",
      "io.circe"           %% "circe-generic"   % "0.8.0",
      "io.circe"           %% "circe-parser"    % "0.8.0",
      "ch.qos.logback"     %  "logback-classic" % "1.1.2",

      "mysql"                %  "mysql-connector-java" % "5.1.34",
      "com.h2database"       %  "h2"                   % "1.4.185",
      "com.typesafe.slick"   %% "slick"                % "3.2.1",
      "com.github.tototoshi" %% "slick-joda-mapper"    % "2.3.0",
      "com.typesafe.slick"   %% "slick-hikaricp"       % "3.2.1",

      "com.typesafe.akka"  %% "akka-http"       % "10.0.10",

      "org.scalatest"     %% "scalatest"         % "3.0.3"   % "it,test",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % "test"
    )
  )

enablePlugins(DockerPlugin)

dockerfile in docker := {
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}

imageNames in docker := Seq(
  ImageName(s"scout24/${name.value}:latest"),
  ImageName(
    namespace = Some("scout24"),
    repository = name.value,
    tag = Some("v" + version.value)
  )
)

buildOptions in docker := BuildOptions(cache = false)
