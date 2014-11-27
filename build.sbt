organization := "com.acme"

name := "scala-json"

version := "1.0"

scalaVersion := "2.11.2"


resolvers  ++= Seq(
//	"Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/",
//	"Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
//	"Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
  "snapshots"           at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"            at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
	"com.typesafe.play"          %% "play-json" % "2.4.0-M1",
	//"org.scalatest" %% "scalatest" % "2.2.1" % "test"
  "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test"
)
