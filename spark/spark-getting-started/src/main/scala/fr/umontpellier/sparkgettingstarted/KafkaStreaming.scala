import org.apache.spark.sql.SparkSession

/**
  * This application reads a stream of movies in json format from a Kafka topic.
  * In parallel, it writes the data to a parquet file in an S3 bucket.
  */
object KafkaStreaming {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("KafkaStreaming")
      .getOrCreate()

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "my-cluster-kafka-bootstrap.kafka.svc.cluster.local:9092")
      .option("subscribe", "movies")
      .option("startingOffsets", "latest")
      .load()

    import spark.implicits._

    val movieJson = df.selectExpr("CAST(value AS STRING)")
    
    val movieDF = movieJson.select(
      org.apache.spark.sql.functions.from_json(
        $"value", 
        org.apache.spark.sql.types.StructType(Seq(
          org.apache.spark.sql.types.StructField("id", org.apache.spark.sql.types.IntegerType),
          org.apache.spark.sql.types.StructField("title", org.apache.spark.sql.types.StringType),
          org.apache.spark.sql.types.StructField("country", org.apache.spark.sql.types.StringType),
          org.apache.spark.sql.types.StructField("year", org.apache.spark.sql.types.IntegerType)
        ))
      ).as("movie")
    ).select("movie.*")

    val s3Output = movieDF.writeStream
      .format("json")
      .option("checkpointLocation", "s3a://data/kafka/checkpoints/")
      .outputMode("append")
      .start("s3a://data/kafka/json/")

    spark.streams.awaitAnyTermination()
  }
}
