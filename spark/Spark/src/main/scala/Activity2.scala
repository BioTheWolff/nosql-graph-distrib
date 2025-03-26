package fr.umontpellier.polytech

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkFiles
import org.apache.spark.sql.functions.collect_list

object Activity2 {
  private def processCSV(spark: SparkSession, name: String): Unit = {
    import spark.implicits._
    val users = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(s"file:///${SparkFiles.get(name)}")

    val result = users
      .filter($"age" >= 25)
      .groupBy("city")
      .agg(collect_list("name").as("names"))
      .select("city", "names")

    val resultCollected = result.collect()
    resultCollected.foreach { row =>
      val city = row.getAs[String]("city")
      val names = row.getAs[Seq[String]]("names")
      println(s"Users in $city: ${names.mkString(", ")}")
    }
  }

  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("Needs to provide the filename")
      System.exit(1)
    }

    val filename = args(0)
    val spark = SparkSession.builder()
      .appName("MinIOActivity2")
      .master("local[*]")
      .getOrCreate()

    processCSV(spark, filename)
    spark.stop()
  }
}
