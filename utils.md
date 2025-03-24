
Installation: [https://medium.com/@faaizhussain/installation-of-neo4j-desktop-on-ubuntu-22-04-3d2ea872c5e5](https://medium.com/@faaizhussain/installation-of-neo4j-desktop-on-ubuntu-22-04-3d2ea872c5e5)

Delete all records:
```cypher
match (n) detach delete n
```

Visualise the db:
```cypher
call db.schema.visualization
```

Match with collect: `collect(x) as X`
```cypher
match (m:Movie)<-[r:ACTED_IN]-(a:Person)
with m, collect(a) as actors
where size(actors) > 2
return m.title, size(actors)
```
