package fr.umontpellier.polytech

/* SimpleApp.scala */
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.collect_list

object Activity1 {
  private def one(): Unit = {
    val logFile = "./data/spark.txt" // Should be some file on your system
    val spark = SparkSession.builder.appName("Simple Application").master("local[*]").getOrCreate()
    val logData = spark.read.textFile(logFile).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    spark.stop()
  }

  private def two(): Unit = {

    val spark = SparkSession.builder()
      .appName("UsersTest")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._
    val usersDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/users.csv")

    val result = usersDF
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
    spark.stop()
  }

  def main(args: Array[String]): Unit = {
    one()
    two()
  }
}