# Neo4j

## Setup

```bash
just deploy
```

## Delete

```bash
just delete
```

## Query

```bash
just pf
```

Go to `http://localhost:7474` and use the credentials `neo4j / supersecret` to connect to the database.

## DDL

```bash
CREATE (:Person {id: 1, name: "Robert de Niro", age: 80, salary: 150000})-[:ACTED_IN]->(:Movie {title: "Brazil"})<-[:LIKES]-(:Person {id: 2, name: "Martin", age: 20, salary: 45000})-[:STUDIED_AT]->(:School {name: "Joliot Curie Aubagne"}),
(:Activity {name: "Escalade"})<-[:PRACTICES {since: 2024}]-(:Person {id: 2, name: "Martin", age: 20, salary: 45000})-[:WRITES_CODE {since: 2023}]->(:ProgrammingLanguage {name: "Rust"})

CREATE (:Person {id: 3, name: "Matéo", age: 22, salary: 48000})-[:WRITES_CODE {since: 2022}]->(:ProgrammingLanguage {name: "Rust"});

CREATE (:Person {id: 4, name: "Noé", age: 22, salary: 47000})-[:WRITES_CODE {since: 2023}]->(:ProgrammingLanguage {name: "Go"});
```

## Sample Queries

### People who write Go

```cypher
MATCH (p:Person)-[r:WRITES_CODE]->(pl:ProgrammingLanguage)
WHERE pl.name = 'Go'
RETURN p, pl;
```

### People who likes movies

```cypher
MATCH (p:Person)-[r:LIKES]->(m:Movie)
RETURN p, m;
```

## Drop the db

```bash
MATCH (n) DETACH DELETE n;
```

## Movies

```bash
cat movies.cypher
```

### Sample Query

#### How many movies were released between 2005 and 2010?

```cypher
MATCH (m:Movie) WHERE m.released > 2005 AND m.released < 2010 RETURN COUNT(m);
```

#### Movies with more than 1 actor

```cypher
MATCH (m:Movie)<-[r]-(p:Person)
WITH m, COUNT(r) as cnt
WHERE cnt > 1
RETURN m.title, cnt as numberactor;
```

#### Movies released the earliest and the latest

```cypher
MATCH (m:Movie)
RETURN MIN(m.released), MAX(m.released);
```

#### Create a new movie

```cypher
CREATE (RedLine:Movie {title: 'Red Line', released: 2008, tagline: 'On the planet Dorothy, "Sweet" JP races in the Yellowline car race, the final elimination race to the most popular race in the galaxy, the Redline.'})
CREATE (JP:Person {name: 'JP', born: 1980})
CREATE
(JP)-[:ACTED_IN {roles:['JP']}]->(RedLine);
```

#### Edit a movie

```cypher
MATCH (m:Movie {title: 'Red Line'})
SET m.released = 2007
RETURN m;
```

#### Delete a movie

```cypher
MATCH (m:Movie {title: 'Red Line'}) DETACH DELETE m;
```

#### Delete a person

```cypher
MATCH (p:Person {name: 'JP'}) DETACH DELETE p;
```
