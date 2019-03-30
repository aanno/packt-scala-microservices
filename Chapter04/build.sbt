import sbt.Keys._

name := "chapter-4"

version := "1.0"

lazy val `chapter-4` = (project in file(".")).aggregate(
  `web-app`,
  `auth-app`,
  `so-app`,
  `github-app`,
  `rank-app`,
   commons)

lazy val commonSettings = Seq(
  organization := "com.scalamicroservices",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
  scalaVersion := "2.12.8",
  resolvers ++= Seq("Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    "JBoss" at "https://repository.jboss.org/")
)

lazy val commons = BaseProject("commons")
  .settings(libraryDependencies ++= Seq(specs2 % Test, playJson))


lazy val `web-app` = PlayProject("web-app")
  .settings(libraryDependencies ++= Seq(parserCombinator, ws, specs2 % Test, guice, scalaTest))
  .dependsOn(commons)

lazy val `so-app` = PlayProject("so-app")
  .settings(libraryDependencies ++= Seq(h2, jbcrypt, slick, playSlick, playSlickEvolutions, guice, specs2 % Test))
  .dependsOn(commons)

lazy val `auth-app` = PlayProject("auth-app")
  .settings(libraryDependencies ++= Seq(h2, jbcrypt, slick, playSlick, playSlickEvolutions, guice, specs2 % Test))
  .dependsOn(commons)

lazy val `rank-app` = PlayProject("rank-app")
  .settings(libraryDependencies ++= Seq(guice))
  .dependsOn(commons)

lazy val `github-app` = PlayProject("github-app")
  .settings(libraryDependencies ++= Seq(h2, jbcrypt, slick, playSlick, playSlickEvolutions, guice))
  .dependsOn(commons)


def BaseProject(name: String): Project = (
  Project(name, file(name))
    settings (commonSettings: _*)
  )

def PlayProject(name: String): Project = (
  BaseProject(name)
    enablePlugins PlayScala
  )

val slickV = "3.3.0"
val h2V = "1.4.199"
val playSlickV = "4.0.0"
val jbcryptV = "0.4"
val parserCombinatorV = "1.1.1"

val slick = "com.typesafe.slick" %% "slick" % slickV
val slickHikariCP = "com.typesafe.slick" %% "slick-hikaricp" % slickV
val h2 = "com.h2database" % "h2" % h2V
val playSlick = "com.typesafe.play" %% "play-slick" % playSlickV
val playSlickEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % playSlickV
val jbcrypt = "org.mindrot" % "jbcrypt" % jbcryptV
// val parserCombinator = "org.scala-lang.modules" % "scala-parser-combinators_2.12" % parserCombinatorV
val parserCombinator = "org.scala-lang.modules" %% "scala-parser-combinators" % parserCombinatorV
val playJson = "com.typesafe.play" %% "play-json" % "2.7.2"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.7" % "test"
val runAll = inputKey[Unit]("Runs all subprojects")


runAll := {
  (run in Compile in `web-app`).partialInput(" 3000").evaluated
  (run in Compile in `so-app`).partialInput(" 5000").evaluated
  (run in Compile in `auth-app`).partialInput(" 5001").evaluated
}

fork in run := true

// enables unlimited amount of resources to be used :-o just for runAll convenience
concurrentRestrictions in Global := Seq(
  Tags.customLimit(_ => true)
)
