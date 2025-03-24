
# Activity 4: StackOverflow

First, let's parse the raw JSON in steps:

```cypher
call apoc.load.jsonArray("raw.json", "$.items") yield value
```

```cypher
call apoc.load.jsonArray("raw.json", "$.items")
yield value
unwind value.answers as answers
return value, answers
```
