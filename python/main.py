from neo4j import GraphDatabase
import os

URI = os.getenv("NEO4J_URI", "bolt://localhost:7687")
AUTH = (os.getenv("NEO4J_USER", "neo4j"), os.getenv("NEO4J_PASSWORD", "neo4j"))

with GraphDatabase.driver(URI, auth=AUTH) as driver:
    records, summary, keys = driver.execute_query(
        "MATCH (p:Person) RETURN p"
    )
    for record in records:
        print(record)
