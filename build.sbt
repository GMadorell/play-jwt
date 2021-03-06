name := """play-jwt-test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

// JWT
libraryDependencies += "com.jason-goodwin" %% "authentikat-jwt" % "0.4.1"

// Dependency injection
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.0.0"

// Hashing
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

// Test
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalatestplus" %% "play" % "1.4.0-M3" % "test",
  "org.mockito" % "mockito-core" % "1.8.5"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
