package fr.umontpellier.polytech

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{avg, col, collect_list, from_csv}
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

import java.util


object Activity7Kafka {

  private val schema = new StructType()
    .add("id", IntegerType, nullable = true)
    .add("name", StringType, nullable = true)
    .add("age", IntegerType, nullable = true)
    .add("city", StringType, nullable = true)

  private def helper(): SparkSession = {
    SparkSession.builder()
      .appName("UserStreamingKafka")
      .master("local[*]")
      .getOrCreate()
  }

  def main(args: Array[String]): Unit = {
    val spark = helper()
    spark.sparkContext.setLogLevel("ERROR")

    import spark.implicits._

    // Only listen to topics starting with streaming.* (e.g. streaming.test)
    // This outputs the raw value in the topic, still needs to be refined afterwards
    val raw = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribePattern", "streaming.*")
      .option("kafka.group.id", "spark-streaming")
      .load()
      .selectExpr("CAST(value AS STRING)")
      .as[String]

    // Transforming the raw value into bronze refined (csv columns)
    val bronze = raw
      .withColumn("users", from_csv(col("value"), schema, Map[String, String]()))
      .select(col("users.*"))

    val gold = bronze
      .distinct()
      .groupBy("city")
      .agg(
        collect_list("name").as("names"),
        avg("age").as("average_age")
      )

    gold.writeStream
      .outputMode("complete")
      .format("console")
      .trigger(Trigger.ProcessingTime("1 second"))
      .start()
      .awaitTermination()
  }
}
