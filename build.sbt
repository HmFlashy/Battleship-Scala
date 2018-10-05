import Dependencies._

name := """Battleship-HugoMaitre"""

version := "1.0"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "me.hugomaitre",
      scalaVersion := "2.12.5",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Battleship",
    libraryDependencies ++=  Seq(
      scalaTest % Test
    )
)
