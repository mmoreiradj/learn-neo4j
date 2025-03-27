ARG SPARK_IMAGE=bitnami/spark:3.5.5-debian-12-r1
FROM ${SPARK_IMAGE}

COPY target/scala-2.12/spark-getting-started-assembly-0.0.1.jar /opt/spark/app/jars/app.jars

USER root
RUN chmod +x /opt/spark/app/jars/app.jars
