organization := "com.acme"

name := "scala-json"

version := "1.0"

scalaVersion := "2.10.1"

// Mandubian repos for standalone play-json
resolvers  ++= Seq(
	"Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/",
	"Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/"
)

libraryDependencies ++= Seq(
	"play"        %% "play-json" % "2.2-SNAPSHOT",
	"org.specs2"  %% "specs2"    % "1.13" % "test",
    "junit"        % "junit"     % "4.8"  % "test"
)
