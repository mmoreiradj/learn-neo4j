// import org.apache.spark.sql.{SparkSession, Dataset}
// import org.apache.spark.sql.functions._

// object MongoDB {
//   def main(args: Array[String]): Unit = {
//     val spark = SparkSession.builder
//       .appName("MongoDB")
//       .getOrCreate()

//     val df = spark.read.format("mongodb")
//       .option("database", "datascience")
//       .option("collection", "people")
//       .load()

//     df.show()

//     spark.stop()
//   }
// }