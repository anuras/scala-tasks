import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

assemblySettings

name := "akka-aggregator"

jarName in assembly := "akka-aggregator.jar"

version := "1.0"

scalaVersion := "2.11.6"

lazy val akkaVersion = "2.4.0"

resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Concurrent Maven Repo" at "http://conjars.org/repo",
  "Clojars Repository" at "http://clojars.org/repo",
  "Twitter Maven" at "http://maven.twttr.com",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)

test in assembly := {}