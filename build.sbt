name := "thing"

version := "3.1"

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Xlint",
  "-Xfatal-warnings"
)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"           % "3.1.1",
  "org.postgresql"      % "postgresql"      % "9.4.1208",
  "ch.qos.logback"      % "logback-classic" % "1.1.2"
)

