organization := "com.acme"

name := "scala-json"

version := "1.0"

scalaVersion := "2.11.4"


resolvers ++= Seq(
  //	"Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/",
  //	"Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
  //	"Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
  //Resolver.sonatypeRepo("snapshots"),
  //  Resolver.sonatypeRepo("releases"),
 "bintray" at "http://dl.bintray.com/scalaz/releases",
 // "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
 // "releases" at "http://oss.sonatype.org/content/repositories/releases",
 "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.3.6",
  "org.scalatest" % "scalatest_2.11" % "2.2.2" % Test,
  "junit" % "junit" % "4.8.2" % Test,
//  "org.specs2" % "specs2_2.11" % "2.4.13",
//  "org.specs2" % "specs2_2.11" % "2.4.9-scalaz-7.0.6",
//  "org.specs2"      %% "specs2-core"           % "2.4.13",
//  "org.specs2"      %% "specs2-matcher-extra"  % "2.4.13",
//  "org.specs2"      %% "specs2-gwt"            % "2.4.13",
//  "org.specs2"      %% "specs2-html"           % "2.4.13",
//  "org.specs2"      %% "specs2-form"           % "2.4.13",
//  "org.specs2"      %% "specs2-scalacheck"     % "2.4.13",
//  "org.specs2"      %% "specs2-mock"           % "2.4.13",
//  "org.specs2"      %% "specs2-junit"          % "2.4.13"
     "org.scalatest" %% "scalatest" % "2.2.1" % Test,
    "org.specs2"      %% "specs2-core"           % "2.5-SNAPSHOT" % Test,
    "org.specs2"      %% "specs2-matcher-extra"  % "2.5-SNAPSHOT" % Test,
    "org.specs2"      %% "specs2-gwt"            % "2.5-SNAPSHOT" % Test,
    "org.specs2"      %% "specs2-html"           % "2.5-SNAPSHOT" % Test,
    "org.specs2"      %% "specs2-form"           % "2.5-SNAPSHOT" % Test,
    "org.specs2"      %% "specs2-scalacheck"     % "2.5-SNAPSHOT" % Test,
    "org.specs2"      %% "specs2-mock"           % "2.5-SNAPSHOT" % Test,
    "org.specs2"      %% "specs2-junit"          % "2.5-SNAPSHOT" % Test
)
