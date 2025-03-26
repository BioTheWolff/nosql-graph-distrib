package fr.umontpellier.polytech

/* SimpleApp.scala */
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Activity4 {

  private def query(spark: SparkSession, qs: String): DataFrame = {
    spark.read
      .format("org.neo4j.spark.DataSource")
      .option("query", qs)
      .load()
  }

  private def process(spark: SparkSession): Unit = {
    query(spark, "match (n) return count(n)").show()
  }


  def main(args: Array[String]): Unit = {
    val url = "neo4j://localhost:7687"
    val username = "neo4j"
    val password = "testtest"
    val dbname = "prout"

    val spark = SparkSession.builder()
      .config("neo4j.url", url)
      .config("neo4j.authentication.basic.username", username)
      .config("neo4j.authentication.basic.password", password)
      .config("neo4j.database", dbname)
      .appName("StackOverflow")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    process(spark)

    spark.stop()
  }
}
