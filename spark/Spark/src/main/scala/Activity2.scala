package fr.umontpellier.polytech

import org.apache.spark.sql.SparkSession
import com.amazonaws.SDKGlobalConfiguration
import org.apache.spark.sql.functions.collect_list

object Activity2 {
  private def helper(endpoint: String): SparkSession = {
    System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true")

    val user = "minio"
    val password = "minio123"

    SparkSession.builder()
      .appName("MinIOActivity2")
      .master("local[*]")
      .config("spark.hadoop.fs.s3a.endpoint", endpoint)
      .config("spark.hadoop.fs.s3a.access.key", user)
      .config("spark.hadoop.fs.s3a.secret.key", password)
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.connection.timeout", "100000")
      .getOrCreate()
  }

  private def processCSV(spark: SparkSession): Unit = {

    val path = "s3a://mybucket/users.csv"

    import spark.implicits._
    val users = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(path)

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
    var endpoint = "https://minio.s3.svc.cluster.local:9000/"
    if (args.length > 0)
      endpoint = args(0)

    println(s"Using endpoint: $endpoint")

    val spark = helper(endpoint)

    processCSV(spark)

    spark.stop()
  }
}
