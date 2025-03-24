# Activity 2

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
