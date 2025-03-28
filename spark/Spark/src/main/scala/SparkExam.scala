package fr.umontpellier.polytech

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkExam {

  private def query(spark: SparkSession, qs: String): DataFrame = {
    spark.read
      .format("org.neo4j.spark.DataSource")
      .option("query", qs)
      .load()
  }

  private def examTask(spark: SparkSession): Unit = {
    // Question 5. Find out what types of transactions do these Clients perform with first party fraudsters?
    query(
      spark,
      """match (:Client:FirstPartyFraudster)-[]-(t:Transaction)-[]-(c:Client)
        |where not c:FirstPartyFraudster
        |unwind labels(t) as lab
        |return lab, count(t)""".stripMargin
    ).show()
  }


  def main(args: Array[String]): Unit = {
    if (args.length < 3) {
      println("Please provide program arguments: URL, username, password (in this order)")
      System.exit(1)
    }

    val url = args(0)
    val username = args(1)
    val password = args(2)
    val dbname = "neo4j"

    val spark = SparkSession.builder()
      .config("neo4j.url", url)
      .config("neo4j.authentication.basic.username", username)
      .config("neo4j.authentication.basic.password", password)
      .config("neo4j.database", dbname)
      .appName("SparkExam")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    examTask(spark)

    spark.stop()
  }
}
