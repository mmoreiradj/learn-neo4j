import org.apache.spark.sql.SparkSession

object Neo4JDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("Neo4J")
      .config("neo4j.url", "bolt://spark-neo4j.spark.svc.cluster.local:7687")
      .config("neo4j.authentication.basic.username", "neo4j")
      .config("neo4j.authentication.basic.password", "neo4j-admin")
      .config("neo4j.database", "neo4j")
      .getOrCreate()

    spark.read.format("org.neo4j.spark.DataSource")
      .option("labels", "User")
      .load()
      .show()
  }
}
