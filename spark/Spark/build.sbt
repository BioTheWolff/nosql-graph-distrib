ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "2.12.18"

ThisBuild / libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.5"

lazy val root = (project in file("."))
  .settings(
    name := "Spark",
    idePackagePrefix := Some("fr.umontpellier.polytech"),
    assembly / mainClass := Some("fr.umontpellier.polytech.Activity2"),
  )

ThisBuild / assemblyMergeStrategy := {
  case x if x.startsWith("META-INF/services/") => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.preferProject
}
