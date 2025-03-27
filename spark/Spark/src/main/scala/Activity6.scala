package fr.umontpellier.polytech

/* SimpleApp.scala */
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{DataFrame, SparkSession}


object Activity6 {

  private val categoryPattern = "\\[\\[Category:(.*?)]]".r
  private val categoryExtraction = udf { text: String =>
    categoryPattern.findAllMatchIn(text).map(_.group(1)).toSeq
  }

  private case class WikiPage(title: String, text: String, isRedirect: Boolean) {}

  private def parsePage(pageString: String): WikiPage = {
    val titleRegex = "<title>(.*?)</title>".r
    val textRegex = "<text>(.*?)</text>".r

    val title = titleRegex.findFirstMatchIn(pageString).map(_.group(1)).getOrElse("")
    val text = textRegex.findFirstMatchIn(pageString).map(_.group(1)).getOrElse("")
    val isRedirect = text.contains("#REDIRECT")

    WikiPage(title, text, isRedirect)
  }

  private def process(spark: SparkSession): Unit = {
    import spark.implicits._
    import org.apache.spark.sql.functions._


    // -- Silver refinement

    val raw = spark.read
      .textFile("data/wikipedia.dat")
      .map(line => line.substring(1, line.length-2))

    val silverRefined = raw.map(parsePage).toDF()
    silverRefined.show(5, truncate = false)


    // -- Gold refinement

    val goldRefined = silverRefined
      .withColumn("categories", categoryExtraction($"text"))
      .withColumn("category", explode(col("categories")))

    goldRefined.groupBy("category")
      .count()
      .orderBy(desc("count"))
      .show(50, truncate = false)
  }


  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Wikipedia")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    val programStartTime = System.nanoTime()

    process(spark)

    val programElapsedTime = (System.nanoTime() - programStartTime) / 1e9
    println(s"\nExecution time: $programElapsedTime seconds")
    println("Program completed successfully!\n")

    spark.stop()
  }
}
