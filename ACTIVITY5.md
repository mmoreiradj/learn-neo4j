# Activity 5

## Data

```cypher
MATCH (n) DETACH DELETE n;

CREATE
    (mateo:User {name: "Matéo" }),
    (adrien:User {name: "Adrien" }),
    (noe:User {name: "Noé" }),
    (muriel:User {name: "Muriel" }),
    (thomas:User {name: "Thomas" }),
    (vincent:User {name: "Vincent" }),
    (martin:User {name: "Martin" }),
    (dziyana:User {name: "Dziyana" }),
    (charley:User {name: "Charley" }),
    (remi:User {name: "Rémi" }),
    (fabien:User {name: "Fabien" }),
    (alexandre:User {name: "Alexandre" }),
    (sylvain:User {name: "Sylvain" }),
    (mathias:User {name: "Mathias" }),
    (m0:Movie {title: "The Matrix", genre: "Action"}),
    (m1:Movie {title: "Inception", genre: "Sci-Fi" }),
    (m2:Movie {title: "The Godfather", genre: "Drama" }),
    (m3:Movie {title: "Forest Gump", genre: "Drama"}),
    (m4:Movie {title: "Interstellar", genre: "Sci-Fi" }),
    (m5:Movie {title: "Mission Impossible", genre: "Action" }),
    (m6:Movie {title: "La La Land", genre: "Musical" }),
    (m7:Movie {title: "The Dark Knight", genre: "Action"}),
    (m8:Movie {title: "Pulp Fiction", genre: "Crime"}),
    (m9:Movie {title: "The Shawshank Redemption", genre: "Drama"}),
    (m10:Movie {title: "Star Wars: A New Hope", genre: "Sci-Fi"}),
    (m11:Movie {title: "The Lion King", genre: "Animation"}),
    (m12:Movie {title: "The Silence of the Lambs", genre: "Thriller"}),
    (m13:Movie {title: "Amélie Poulain", genre: "Romance"}),
    (m14:Movie {title: "The Grand Budapest Hotel", genre: "Comedy"}),
    (mateo)-[:LIKES]->(m0),
    (alexandre)-[:LIKES]->(m2),
    (remi)-[:LIKES]->(m4),
    (thomas)-[:LIKES]->(m4),
    (noe)-[:LIKES]->(m5),
    (muriel)-[:LIKES]->(m1),
    (mathias)-[:LIKES]->(m1),
    (sylvain)-[:LIKES]->(m0),
    (fabien)-[:LIKES]->(m5),
    (vincent)-[:LIKES]->(m4),
    (martin)-[:LIKES]->(m0),
    (adrien)-[:LIKES]->(m4),
    (charley)-[:LIKES]->(m4),
    (dziyana)-[:LIKES]->(m0),
    (mateo)-[:LIKES]->(m7),
    (mateo)-[:LIKES]->(m10),
    (adrien)-[:LIKES]->(m9),
    (adrien)-[:LIKES]->(m13),
    (noe)-[:LIKES]->(m8),
    (noe)-[:LIKES]->(m12),
    (muriel)-[:LIKES]->(m11),
    (muriel)-[:LIKES]->(m14),
    (thomas)-[:LIKES]->(m10),
    (thomas)-[:LIKES]->(m7),
    (vincent)-[:LIKES]->(m9),
    (vincent)-[:LIKES]->(m2),
    (martin)-[:LIKES]->(m8),
    (martin)-[:LIKES]->(m12),
    (dziyana)-[:LIKES]->(m13),
    (dziyana)-[:LIKES]->(m11),
    (charley)-[:LIKES]->(m14),
    (charley)-[:LIKES]->(m3),
    (remi)-[:LIKES]->(m7),
    (remi)-[:LIKES]->(m9),
    (fabien)-[:LIKES]->(m8),
    (fabien)-[:LIKES]->(m12),
    (alexandre)-[:LIKES]->(m10),
    (alexandre)-[:LIKES]->(m11),
    (sylvain)-[:LIKES]->(m13),
    (sylvain)-[:LIKES]->(m14),
    (mathias)-[:LIKES]->(m3),
    (mathias)-[:LIKES]->(m6),
    (mateo)-[:FRIENDS_WITH]->(adrien),
    (mateo)-[:FRIENDS_WITH]->(noe),
    (mateo)-[:FRIENDS_WITH]->(sylvain),
    (adrien)-[:FRIENDS_WITH]->(vincent),
    (adrien)-[:FRIENDS_WITH]->(charley),
    (noe)-[:FRIENDS_WITH]->(fabien),
    (noe)-[:FRIENDS_WITH]->(thomas),
    (muriel)-[:FRIENDS_WITH]->(mathias),
    (muriel)-[:FRIENDS_WITH]->(dziyana),
    (thomas)-[:FRIENDS_WITH]->(remi),
    (vincent)-[:FRIENDS_WITH]->(martin),
    (martin)-[:FRIENDS_WITH]->(alexandre),
    (charley)-[:FRIENDS_WITH]->(remi),
    (fabien)-[:FRIENDS_WITH]->(sylvain),
    (alexandre)-[:FRIENDS_WITH]->(mathias);
```

## Recommendations

### Simple recommendations without considering friendships

#### For Muriel

Here, we use the movies Muriel liked and the movies of users that liked the same movies to recommend movies to Muriel that she hasn't seen yet.

```cypher
MATCH (muriel:User {name: "Muriel"})-[:LIKES]->(liked:Movie) // get muriel's liked movies
MATCH (similar:User)-[:LIKES]->(liked) // get users that liked the same movies
WHERE similar <> muriel // exclude muriel herself
MATCH (similar)-[:LIKES]->(recommended:Movie) // get the movies the other users liked
WHERE NOT (muriel)-[:LIKES]->(recommended) // exclude the movies muriel already liked
RETURN recommended.title as movie_title, recommended.genre as genre, COUNT(DISTINCT similar) as recommendation_score, 
       COLLECT(DISTINCT similar.name) as based_on_users // add the users the recommendation is based on
ORDER BY recommendation_score DESC;
```

Results:

╒═══════════════════════╤═════════╤════════════════════╤══════════════════════╕
│movie_title            │genre    │recommendation_score│based_on_users        │
╞═══════════════════════╪═════════╪════════════════════╪══════════════════════╡
│"Amélie Poulain"       │"Romance"│2                   │["Dziyana", "Sylvain"]│
├───────────────────────┼─────────┼────────────────────┼──────────────────────┤
│"The Matrix"           │"Action" │2                   │["Dziyana", "Sylvain"]│
├───────────────────────┼─────────┼────────────────────┼──────────────────────┤
│"Forest Gump"          │"Drama"  │2                   │["Mathias", "Charley"]│
├───────────────────────┼─────────┼────────────────────┼──────────────────────┤
│"The Godfather"        │"Drama"  │1                   │["Alexandre"]         │
├───────────────────────┼─────────┼────────────────────┼──────────────────────┤
│"Star Wars: A New Hope"│"Sci-Fi" │1                   │["Alexandre"]         │
├───────────────────────┼─────────┼────────────────────┼──────────────────────┤
│"La La Land"           │"Musical"│1                   │["Mathias"]           │
├───────────────────────┼─────────┼────────────────────┼──────────────────────┤
│"Interstellar"         │"Sci-Fi" │1                   │["Charley"]           │
└───────────────────────┴─────────┴────────────────────┴──────────────────────┘

#### For Dziyana

Same as above but for Dziyana.

```cypher
MATCH (dziyana:User {name: "Dziyana"})-[:LIKES]->(liked:Movie)
MATCH (similar:User)-[:LIKES]->(liked)
WHERE similar <> dziyana
MATCH (similar)-[:LIKES]->(recommended:Movie)
WHERE NOT (dziyana)-[:LIKES]->(recommended)
RETURN recommended.title as movie_title, recommended.genre as genre, COUNT(DISTINCT similar) as recommendation_score, 
       COLLECT(DISTINCT similar.name) as based_on_users
ORDER BY recommendation_score DESC;
```

Results:

╒══════════════════════════╤══════════╤════════════════════╤══════════════════════╕
│movie_title               │genre     │recommendation_score│based_on_users        │
╞══════════════════════════╪══════════╪════════════════════╪══════════════════════╡
│"Star Wars: A New Hope"   │"Sci-Fi"  │2                   │["Alexandre", "Matéo"]│
├──────────────────────────┼──────────┼────────────────────┼──────────────────────┤
│"The Grand Budapest Hotel"│"Comedy"  │2                   │["Muriel", "Sylvain"] │
├──────────────────────────┼──────────┼────────────────────┼──────────────────────┤
│"The Godfather"           │"Drama"   │1                   │["Alexandre"]         │
├──────────────────────────┼──────────┼────────────────────┼──────────────────────┤
│"Inception"               │"Sci-Fi"  │1                   │["Muriel"]            │
├──────────────────────────┼──────────┼────────────────────┼──────────────────────┤
│"Interstellar"            │"Sci-Fi"  │1                   │["Adrien"]            │
├──────────────────────────┼──────────┼────────────────────┼──────────────────────┤
│"The Shawshank Redemption"│"Drama"   │1                   │["Adrien"]            │
├──────────────────────────┼──────────┼────────────────────┼──────────────────────┤
│"The Silence of the Lambs"│"Thriller"│1                   │["Martin"]            │
├──────────────────────────┼──────────┼────────────────────┼──────────────────────┤
│"Pulp Fiction"            │"Crime"   │1                   │["Martin"]            │
├──────────────────────────┼──────────┼────────────────────┼──────────────────────┤
│"The Dark Knight"         │"Action"  │1                   │["Matéo"]             │
└──────────────────────────┴──────────┴────────────────────┴──────────────────────┘

### Simple recommendations considering friendships with depth of 1

Here, we use the movies of Muriel's friends and the movies of users that liked the same movies to recommend movies to Muriel that she hasn't seen yet.

#### For Muriel

```cypher
MATCH (muriel:User {name: "Muriel"})-[:FRIENDS_WITH*1]-(friend:User) // get muriel's friends
MATCH (friend)-[:LIKES]->(recommended:Movie) // get the movies of muriel's friends
WHERE NOT (muriel)-[:LIKES]->(recommended) // exclude the movies muriel already liked
RETURN recommended.title as movie_title, recommended.genre as genre, COUNT(DISTINCT friend) as recommendation_score, 
       COLLECT(DISTINCT friend.name) as based_on_users
ORDER BY recommendation_score DESC;
```

Results:

╒════════════════╤═════════╤════════════════════╤══════════════╕
│movie_title     │genre    │recommendation_score│based_on_users│
╞════════════════╪═════════╪════════════════════╪══════════════╡
│"La La Land"    │"Musical"│1                   │["Mathias"]   │
├────────────────┼─────────┼────────────────────┼──────────────┤
│"Forest Gump"   │"Drama"  │1                   │["Mathias"]   │
├────────────────┼─────────┼────────────────────┼──────────────┤
│"Amélie Poulain"│"Romance"│1                   │["Dziyana"]   │
├────────────────┼─────────┼────────────────────┼──────────────┤
│"The Matrix"    │"Action" │1                   │["Dziyana"]   │
└────────────────┴─────────┴────────────────────┴──────────────┘

#### For Dziyana

Same as above but for Dziyana.

```cypher
MATCH (dziyana:User {name: "Dziyana"})-[:FRIENDS_WITH*1]-(friend:User)
MATCH (friend)-[:LIKES]->(recommended:Movie)
WHERE NOT (dziyana)-[:LIKES]->(recommended)
RETURN recommended.title as movie_title, recommended.genre as genre, COUNT(DISTINCT friend) as recommendation_score, 
       COLLECT(DISTINCT friend.name) as based_on_users
ORDER BY recommendation_score DESC;
```

Results:

╒══════════════════════════╤════════╤════════════════════╤══════════════╕
│movie_title               │genre   │recommendation_score│based_on_users│
╞══════════════════════════╪════════╪════════════════════╪══════════════╡
│"Inception"               │"Sci-Fi"│1                   │["Muriel"]    │
├──────────────────────────┼────────┼────────────────────┼──────────────┤
│"The Grand Budapest Hotel"│"Comedy"│1                   │["Muriel"]    │
└──────────────────────────┴────────┴────────────────────┴──────────────┘

### Simple recommendations considering friendships with depth of 3

#### For Muriel

```cypher
MATCH (muriel:User {name: "Muriel"})-[:FRIENDS_WITH*1..3]-(friend:User)
MATCH (friend)-[:LIKES]->(recommended:Movie)
WHERE NOT (muriel)-[:LIKES]->(recommended)
RETURN recommended.title as movie_title, recommended.genre as genre, COUNT(DISTINCT friend) as recommendation_score, 
       COLLECT(DISTINCT friend.name) as based_on_users
ORDER BY recommendation_score DESC;
```

Results:

╒══════════════════════════╤══════════╤════════════════════╤═════════════════════╕
│movie_title               │genre     │recommendation_score│based_on_users       │
╞══════════════════════════╪══════════╪════════════════════╪═════════════════════╡
│"The Matrix"              │"Action"  │2                   │["Martin", "Dziyana"]│
├──────────────────────────┼──────────┼────────────────────┼─────────────────────┤
│"La La Land"              │"Musical" │1                   │["Mathias"]          │
├──────────────────────────┼──────────┼────────────────────┼─────────────────────┤
│"Forest Gump"             │"Drama"   │1                   │["Mathias"]          │
├──────────────────────────┼──────────┼────────────────────┼─────────────────────┤
│"The Godfather"           │"Drama"   │1                   │["Alexandre"]        │
├──────────────────────────┼──────────┼────────────────────┼─────────────────────┤
│"Star Wars: A New Hope"   │"Sci-Fi"  │1                   │["Alexandre"]        │
├──────────────────────────┼──────────┼────────────────────┼─────────────────────┤
│"The Silence of the Lambs"│"Thriller"│1                   │["Martin"]           │
├──────────────────────────┼──────────┼────────────────────┼─────────────────────┤
│"Pulp Fiction"            │"Crime"   │1                   │["Martin"]           │
├──────────────────────────┼──────────┼────────────────────┼─────────────────────┤
│"Amélie Poulain"          │"Romance" │1                   │["Dziyana"]          │
└──────────────────────────┴──────────┴────────────────────┴─────────────────────┘

#### For Dziyana

```cypher
MATCH (dziyana:User {name: "Dziyana"})-[:FRIENDS_WITH*1..3]-(friend:User)
MATCH (friend)-[:LIKES]->(recommended:Movie)
WHERE NOT (dziyana)-[:LIKES]->(recommended)
RETURN recommended.title as movie_title, recommended.genre as genre, COUNT(DISTINCT friend) as recommendation_score, 
       COLLECT(DISTINCT friend.name) as based_on_users
ORDER BY recommendation_score DESC;
```

Results:

╒══════════════════════════╤═════════╤════════════════════╤═════════════════════╕
│movie_title               │genre    │recommendation_score│based_on_users       │
╞══════════════════════════╪═════════╪════════════════════╪═════════════════════╡
│"Inception"               │"Sci-Fi" │2                   │["Muriel", "Mathias"]│
├──────────────────────────┼─────────┼────────────────────┼─────────────────────┤
│"The Grand Budapest Hotel"│"Comedy" │1                   │["Muriel"]           │
├──────────────────────────┼─────────┼────────────────────┼─────────────────────┤
│"La La Land"              │"Musical"│1                   │["Mathias"]          │
├──────────────────────────┼─────────┼────────────────────┼─────────────────────┤
│"Forest Gump"             │"Drama"  │1                   │["Mathias"]          │
├──────────────────────────┼─────────┼────────────────────┼─────────────────────┤
│"The Godfather"           │"Drama"  │1                   │["Alexandre"]        │
├──────────────────────────┼─────────┼────────────────────┼─────────────────────┤
│"Star Wars: A New Hope"   │"Sci-Fi" │1                   │["Alexandre"]        │
└──────────────────────────┴─────────┴────────────────────┴─────────────────────┘

### If we know that vincent likes science fiction how can we recommend movies in the same genre

```cypher
MATCH (vincent:User {name: "Vincent"})-[:LIKES]->(liked:Movie {genre: "Sci-Fi"})
MATCH (movie:Movie {genre: "Sci-Fi"})
WHERE liked <> movie
RETURN movie
```

Results:

╒═════════════════════════════════════════════════════════╕
│movie                                                    │
╞═════════════════════════════════════════════════════════╡
│(:Movie {genre: "Sci-Fi",title: "Inception"})            │
├─────────────────────────────────────────────────────────┤
│(:Movie {genre: "Sci-Fi",title: "Star Wars: A New Hope"})│
└─────────────────────────────────────────────────────────┘

We can check that the result is right by looking at the movies Vincent liked:

```cypher
MATCH (vincent:User {name: "Vincent"})-[:LIKES]->(liked:Movie)
RETURN liked
```

Results:

╒═══════════════════════════════════════════════════════════╕
│liked                                                      │
╞═══════════════════════════════════════════════════════════╡
│(:Movie {genre: "Sci-Fi",title: "Interstellar"})           │
├───────────────────────────────────────────────────────────┤
│(:Movie {genre: "Drama",title: "The Godfather"})           │
├───────────────────────────────────────────────────────────┤
│(:Movie {genre: "Drama",title: "The Shawshank Redemption"})│
└───────────────────────────────────────────────────────────┘

They indeed do not contain the movies "Inception" and "Star Wars: A New Hope".
