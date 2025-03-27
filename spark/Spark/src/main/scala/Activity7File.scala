package fr.umontpellier.polytech

import org.apache.spark.sql.functions.{avg, collect_list}
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}


object Activity7File {

  private def helper(): SparkSession = {
    val user = "minio"
    val password = "minio123"

    SparkSession.builder()
      .appName("UserStreamingFile")
      .master("local[*]")
      .config("spark.hadoop.fs.s3a.endpoint", "http://localhost:9000")
      .config("spark.hadoop.fs.s3a.access.key", user)
      .config("spark.hadoop.fs.s3a.secret.key", password)
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.connection.timeout", "100000")
      .getOrCreate()
  }

  def main(args: Array[String]): Unit = {
    val spark = helper()
    spark.sparkContext.setLogLevel("ERROR")

    val schema = new StructType()
      .add("id", IntegerType, nullable = true)
      .add("name", StringType, nullable = true)
      .add("age", IntegerType, nullable = true)
      .add("city", StringType, nullable = true)

    val readStream = spark.readStream
      .format("csv")
      .schema(schema)
      .load("s3a://streaming/")

    val result = readStream
      .distinct()
      .groupBy("city")
      .agg(
        collect_list("name").as("names"),
        avg("age").as("average_age")
      )

    result.writeStream
      .outputMode("complete")
      .format("console")
      .trigger(Trigger.ProcessingTime("1 second"))
      .start()
      .awaitTermination()
  }
}
