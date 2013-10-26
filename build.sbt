organization := "com.acme"

name := "scala-json"

version := "1.0"

scalaVersion := "2.10.3"


resolvers  ++= Seq(
	"Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/",
	"Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
	"Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
)

libraryDependencies ++= Seq(
	"play"          %% "play-json" % "2.2-SNAPSHOT",
	"org.scalatest" %% "scalatest" % "1.9.1" % "test"
)
