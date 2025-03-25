ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

ThisBuild / libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.5"

lazy val root = (project in file("."))
  .settings(
    name := "Spark",
    idePackagePrefix := Some("fr.umontpellier.polytech")
  )
