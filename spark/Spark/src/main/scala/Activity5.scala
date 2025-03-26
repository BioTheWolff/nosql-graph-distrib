package fr.umontpellier.polytech

/* SimpleApp.scala */
import org.apache.spark.sql.{DataFrame, SparkSession}

object Activity5 {

  private def query(spark: SparkSession): DataFrame = {
    spark.read
      .format("mongodb")
      .option("database", "test")
      .option("collection", "collection")
      .load()
  }

  private def process(spark: SparkSession): Unit = {
    val df = query(spark)

    df.show(5)
    println(s"Collection has ${df.count()} documents")
  }


  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .config("spark.mongodb.read.connection.uri", "mongodb://127.0.0.1:27017/test.collection")
      .config("spark.mongodb.write.connection.uri", "mongodb://127.0.0.1:27017/test.collection")
      .appName("StackOverflow")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    process(spark)

    spark.stop()
  }
}
