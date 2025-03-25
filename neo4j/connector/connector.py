import os
from neo4j import GraphDatabase
from dotenv import load_dotenv

load_dotenv()


URI = os.getenv("NEO4J_URI")
USER = os.getenv("NEO4J_USER")
PASSWORD = os.getenv("NEO4J_PASSWORD")
DB = os.getenv("NEO4J_DB")
AUTH = (USER, PASSWORD)


with GraphDatabase.driver(URI, auth=AUTH) as driver:
    driver.verify_connectivity()

    records, summary, keys = driver.execute_query(
        "RETURN COUNT {()} AS count", database_=DB
    )

    print(records)

    driver.close()