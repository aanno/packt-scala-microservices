logLevel := Level.Warn

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += "rediscala" at "http://dl.bintray.com/etaty/maven"

addSbtCoursier
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.0")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")
