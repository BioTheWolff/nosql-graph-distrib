
Load the JSON file using APOC:

```cypher
call apoc.load.json("apoc.json") yield value
```


To use the Python connector, in the [connector folder](../connector/)

Write the variables to the env file:
```ini
NEO4J_URI=bolt://localhost:7687
NEO4J_USER=neo4j
NEO4J_PASSWORD=example
```

Then use the connector to load the JSON and return the CVE count:
```cypher
WITH "file:///nvdcve-1.1-2018.json" as url 
CALL apoc.load.json(url) YIELD value 
UNWIND  value.CVE_Items as cve
RETURN count (cve)
```
