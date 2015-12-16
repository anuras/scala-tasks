import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

assemblySettings

jarName in assembly := "spark-aggregator.jar"

name := "spark-aggregator"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.5.1" % "provided",
  "org.specs2" %% "specs2" % "2.4.2" % "test",
  "com.github.scala-incubator.io" %% "scala-io-file"      % "0.4.3-1"
)

resolvers           ++= Seq(
  "Concurrent Maven Repo" at "http://conjars.org/repo",
  "Maven Repository" at "https://repo1.maven.org/maven2",
  "Akka Repository" at "http://repo.akka.io/releases/",
  "Spray Repository" at "http://repo.spray.cc/"
)

test in assembly := {}

resolvers += Resolver.sonatypeRepo("public")

//run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))