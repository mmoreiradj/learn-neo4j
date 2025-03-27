// give the user a nice default project!

val sparkVersion = settingKey[String]("Spark version")

lazy val root = (project in file(".")).

  settings(
    inThisBuild(List(
      organization := "fr.umontpellier",
      scalaVersion := "2.12.13"
    )),
    name := "spark-getting-started",
    version := "0.0.1",

    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    javaOptions ++= Seq("-Xms512M", "-Xmx2048M"),
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    parallelExecution in Test := false,
    fork := true,

    coverageHighlighting := true,

    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-streaming" % "3.5.5" % "provided",
      "org.apache.spark" %% "spark-sql" % "3.5.5" % "provided",
      "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.5.5",
      "org.apache.hadoop" % "hadoop-common" % "3.4.1" % "provided",
      "org.apache.hadoop" % "hadoop-aws" % "3.4.1" % "provided",
      "com.amazonaws" % "aws-java-sdk-bundle" % "1.12.782" % "provided",
      "org.mongodb.spark" % "mongo-spark-connector_2.12" % "10.4.1",
      "org.neo4j" % "neo4j-connector-apache-spark_2.12" % "5.3.5_for_spark_3" excludeAll(
        ExclusionRule(organization = "io.netty", name = "netty-handler"),
        ExclusionRule(organization = "io.netty", name = "netty-common")
      ),

      "org.scalatest" %% "scalatest" % "3.2.2" % "test",
      "org.scalacheck" %% "scalacheck" % "1.15.2" % "test",
      "com.holdenkarau" %% "spark-testing-base" % "3.3.0_1.3.0" % "test"
    ),

    // uses compile classpath for the run task, including "provided" jar (cf http://stackoverflow.com/a/21803413/3827)
    run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated,

    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    pomIncludeRepository := { x => false },

    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", "native-image", "org.mongodb", "bson", "native-image.properties") => MergeStrategy.first
      case PathList("META-INF", "versions", "11", "module-info.class") => MergeStrategy.first
      case x => (assemblyMergeStrategy in assembly).value(x)
    },

    resolvers ++= Seq(
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
      "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
      "Second Typesafe repo" at "https://repo.typesafe.com/typesafe/maven-releases/",
      Resolver.sonatypeRepo("public")
    ),

    pomIncludeRepository := { _ => false },

    // publish settings
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    }
  )
