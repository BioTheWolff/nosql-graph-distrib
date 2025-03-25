CREATE (n2:University {city: "Montpellier", name: "University of Montpellier"})<-[:STUDIES]-(n0:Person {name: "Fabien", age: 21})-[:KNOWS {since: 2024}]->(:Person {name: "Arsène", age: 23})-[:STUDIES]->(n2)<-[:HAS_STUDENTS]-(:Studio {name: "Ubisoft", city: "Montpellier"})-[:DEVELOPS]->(n3:`Video game` {name: "AC Shadows", date: "20/03/2025"}),
(n0)-[:PLAYS]->(n3)
