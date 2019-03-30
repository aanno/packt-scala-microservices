name := "chriper-app-primer"

organization in ThisBuild := "sample.chirper"

// scalaVersion := "2.11.12"
scalaVersion in ThisBuild := "2.12.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.2" % "provided"

lazy val friendApi = project("friend-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomScaladslApi
  )

lazy val friendImpl = project("friend-impl")
  .enablePlugins(LagomScala)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(friendApi)

lazy val chirpApi = project("chirp-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val chirpImpl = project("chirp-impl")
  .enablePlugins(LagomScala)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslPubSub,
      lagomScaladslTestKit,
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(chirpApi)

lazy val activityStreamApi = project("activity-stream-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomScaladslApi
  )
  .dependsOn(chirpApi)

lazy val activityStreamImpl = project("activity-stream-impl")
  .enablePlugins(LagomScala)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire
    )
  )
  .dependsOn(activityStreamApi, chirpApi, friendApi)

lazy val frontEnd = project("front-end")
  .enablePlugins(PlayScala, LagomPlay)
  .settings(
    version := "1.0-SNAPSHOT",
    routesGenerator := StaticRoutesGenerator,
    libraryDependencies ++= Seq(
      "org.webjars.npm" % "react" % "16.8.5",
      "org.webjars.npm" % "react-router" % "5.0.0",
      "org.webjars.npm" % "jquery" % "3.3.1",
      "org.webjars.npm" % "foundation-sites" % "6.3.1",
      macwire,
      lagomScaladslServer
    )
    //, ReactJsKeys.sourceMapInline := true
  )

def project(id: String) = Project(id, base = file(id))
  .settings(
    scalacOptions in Compile += "-Xexperimental" // this enables Scala lambdas to be passed as Scala SAMs  
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8" // actually, only api projects need this
    )
  )

licenses in ThisBuild := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

lagomCassandraEnabled in ThisBuild := false

lagomKafkaEnabled in ThisBuild := false
