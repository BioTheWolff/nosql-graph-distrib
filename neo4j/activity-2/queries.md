
Using Cypher model defined in activity one ([here](../activity-1/model.cypher)).


Query a person:
```cypher
match (n:Person) return n
```

With edges:
```cypher
match (a:Person)-[r:KNOWS]->(b:Person)
return a,r,b
```

Values:
```cypher
match (p:Person)-[r:STUDIES]->(u:University)
return p.name, r.since, u.name
```

Properties:
```cypher
match (p:Person)-[r:STUDIES]->(u:University)
where p.name = "Fabien"
return p.name, r.since, u.name
```