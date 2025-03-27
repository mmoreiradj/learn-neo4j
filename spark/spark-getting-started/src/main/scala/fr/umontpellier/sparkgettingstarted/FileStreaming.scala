import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.Text
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.spark.sql.types.{StructType, IntegerType, StringType}

/**
  * This application reads a stream of CSV files from an S3 bucket and writes the data to a Parquet file in another S3 bucket.
  * It uses a checkpoint location to store the state of the streaming job.
  */
object FileStreaming {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("File Streaming")
      .getOrCreate()

    val schema = new StructType()
      .add("id", IntegerType, nullable = true)
      .add("title", StringType, nullable = true)
      .add("country", StringType, nullable = true)
      .add("year", IntegerType, nullable = true)

    val streamingDf = spark.readStream
      .format("csv")
      .schema(schema)
      .option("header", "true")
      .load("s3a://data/csv/*")

    val s3Output = streamingDf.writeStream
      .format("parquet")
      .option("checkpointLocation", "s3a://data/checkpoints/")
      .outputMode("append")
      .start("s3a://data/parquet/")

    spark.streams.awaitAnyTermination()

    spark.stop()
  }
}
