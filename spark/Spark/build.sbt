ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "2.12.18"

ThisBuild / libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.5",
  "org.apache.spark" %% "spark-sql" % "3.5.5",
  "org.neo4j" %% "neo4j-connector-apache-spark" % "5.3.1_for_spark_3",
  "org.mongodb.spark" % "mongo-spark-connector_2.12" % "10.4.1",
  "org.apache.spark" %% "spark-streaming" % "3.5.5",
  "org.apache.spark" %% "spark-hadoop-cloud" % "3.5.3",
  "io.minio" % "spark-select_2.11" % "2.1"
)

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
