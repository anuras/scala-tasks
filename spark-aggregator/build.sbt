import sbtassembly.Plugin._

assemblySettings

name := "spark-aggregator"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.4.1"
// "org.apache.spark" %% "spark-core" % "1.4.1" % "provided"
  //"org.apache.spark" %% "spark-core" % "1.2.0",
//  "junit" % "junit" % "4.8.1" % "test",
//  "org.apache.spark" %% "spark-mllib" % "1.2.0" % "provided",
//  "com.google.guava" % "guava" % "11.0.1" % "test",
//  "org.specs2" %% "specs2" % "2.4.2" % "test",
//  "com.github.nscala-time" %% "nscala-time" % "1.4.0",
//  "com.twitter" %% "bijection-core" % "0.7.1",
//  "org.scalatest" %% "scalatest" % "2.2.2" % "test",
//  "org.scalacheck" %% "scalacheck" % "1.11.6" % "test",
//  "org.scalaz" %% "scalaz-core" % "7.1.0",
//  "com.github.scala-incubator.io" %% "scala-io-file"      % "0.4.2",
//  "org.apache.commons" % "commons-compress" % "1.9"
)

resolvers           ++= Seq(
  "Concurrent Maven Repo" at "http://conjars.org/repo",
  "Maven Repository" at "https://repo1.maven.org/maven2",
  "Akka Repository" at "http://repo.akka.io/releases/",
  "Spray Repository" at "http://repo.spray.cc/"
)

resolvers += Resolver.sonatypeRepo("public")

//run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))