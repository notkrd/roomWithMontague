import sbt._
import Keys._
import play.sbt._
import Play.autoImport._
import PlayKeys._

lazy val root = (project in file(".")).enablePlugins(PlayScala)
    .settings(
      name := "roomWithMontague",
      scalaVersion := "2.12.5",
      version := "1.0"
    )

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.23s"
      


resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

//unmanagedResourceDirectories in Test +=  baseDirectory ( _ /"" + "target/web/public/test" )
