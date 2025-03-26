package fr.umontpellier.polytech

/* SimpleApp.scala */
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Activity3 {

  private def processStackoverflow(spark: SparkSession, df: DataFrame): Unit = {
    import spark.implicits._

    // First display
    println(s"CSV has ${df.count()} lines")
    df.printSchema()
    df.show(5)

    println(
      //"Count acceptedAnswer null: "+ df.filter(col("acceptedAnswer").isNull).count()
      "\nCount tag null: "+ df.filter($"tag".isNull).count()
        + "\nCount parentId null: "+ df.filter($"parentId".isNull).count() )

    // Filter posts with a score greater than 10
    val highScorePosts = df
      .filter(col("score") > 20)

    highScorePosts.show(5)

    // Query 1: Top 5 highest scores
    val top5Scores = spark.sql("SELECT id, score FROM stackoverflow ORDER BY score DESC LIMIT 5")
    top5Scores.show()

    val top5ScoresWithTag = spark.sql("""
        SELECT id, score, tag
        FROM stackoverflow
        WHERE tag IS NOT NULL
        ORDER BY score DESC
        LIMIT 5
      """)
    top5ScoresWithTag.show()

    // Query: Most frequently used tags
    val popularTags = spark.sql("""
      SELECT tag, COUNT(*) as frequency
      FROM stackoverflow
      WHERE tag IS NOT NULL
      GROUP BY tag
      ORDER BY frequency DESC
      LIMIT 10
    """)
  }

  private def dataframe(spark: SparkSession): DataFrame = {
    val schema = new StructType()
      .add("postTypeId", IntegerType, nullable = true)
      .add("id", IntegerType, nullable = true)
      .add("acceptedAnswer", StringType, nullable = true)
      .add("parentId", IntegerType, nullable = true)
      .add("score", IntegerType, nullable = true)
      .add("tag", StringType, nullable = true)

    spark.read
      .schema(schema)
      .csv("data/stackoverflow.csv")
      .drop("acceptedAnswer")
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("StackOverflow")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    val df = dataframe(spark)
    processStackoverflow(spark, df)

    spark.stop()
  }
}
