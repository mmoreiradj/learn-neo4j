import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions.when

object SimpleApp {
  def main(args: Array[String]): Unit = {
    val csvDataFile = "s3a://data/stackoverflow.csv"

    val spark = SparkSession.builder
      .appName("Simple Application")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

    val schema = new StructType()
      .add("postTypeId", IntegerType, nullable = true)
      .add("id", IntegerType, nullable = true)
      .add("acceptedAnswer", IntegerType, nullable = true)
      .add("parentId", IntegerType, nullable = true)
      .add("score", IntegerType, nullable = true)
      .add("tags", StringType, nullable = true)

    val df = spark.read
      .option("header", "false")
      .schema(schema)
      .csv(csvDataFile)
      .drop("acceptedAnswer")

    println("Number of rows: " + df.count())
    df.printSchema()
    df.show(5)

    import spark.implicits._

    println("Count of null tags: " + df.filter($"tags".isNull).count())
    println("Count of null parentId: " + df.filter($"parentId".isNull).count())

    val highScorePosts = df.filter($"score" > 1000)
    println("Number of posts with score > 1000: " + highScorePosts.count())

    println("First 5 posts with score > 1000:")
    highScorePosts.show(5)

    df.createOrReplaceTempView("stackoverflow")

    spark.sql("""
      SELECT *
      FROM stackoverflow
      ORDER BY score DESC
      LIMIT 5
    """)
    .show()

    spark.sql("""
      SELECT *
      FROM stackoverflow
      WHERE tags != 'NULL' AND tags IS NOT NULL
      ORDER BY score DESC
      LIMIT 5
    """)
    .show()
    

    spark.stop()
  }
}
