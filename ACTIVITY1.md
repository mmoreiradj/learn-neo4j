# Activity 1

## DDL

```bash
CREATE (:Person {id: 1, name: "Robert de Niro", age: 80, salary: 150000})-[:ACTED_IN]->(:Movie {title: "Brazil"})<-[:LIKES]-(:Person {id: 2, name: "Martin", age: 20, salary: 45000})-[:STUDIED_AT]->(:School {name: "Joliot Curie Aubagne"}),
(:Activity {name: "Escalade"})<-[:PRACTICES {since: 2024}]-(:Person {id: 2, name: "Martin", age: 20, salary: 45000})-[:WRITES_CODE {since: 2023}]->(:ProgrammingLanguage {name: "Rust"})

CREATE (:Person {id: 3, name: "Matéo", age: 22, salary: 48000})-[:WRITES_CODE {since: 2022}]->(:ProgrammingLanguage {name: "Rust"});

CREATE (:Person {id: 4, name: "Noé", age: 22, salary: 47000})-[:WRITES_CODE {since: 2023}]->(:ProgrammingLanguage {name: "Go"});
```
