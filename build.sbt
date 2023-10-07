ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

val AkkaVersion = "2.8.5"

lazy val root = (project in file("."))
  .settings(
    name := "Akka Stream POC"
  )

Compile / compile := (Compile / compile).dependsOn(Compile / scalafmtCheckAll).value

resolvers += "Akka library repository".at("https://repo.akka.io/maven")
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion