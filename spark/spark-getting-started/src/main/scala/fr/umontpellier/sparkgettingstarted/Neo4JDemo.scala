// import org.apache.spark.sql.SparkSession

// object Neo4JDemo {
//   def main(args: Array[String]): Unit = {
//     val spark = SparkSession.builder
//       .appName("Neo4J")
//       .getOrCreate()

//     spark.read.format("org.neo4j.spark.DataSource")
//       .option("labels", "User")
//       .load()
//       .show()
//   }
// }
