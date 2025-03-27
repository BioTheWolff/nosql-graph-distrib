
# Activity 3: Spark Streaming

*NB: in this activity, all CSV lines are expected to be WITHOUT headers. So please don't include headers in your csv files in S3, and don't send the headers in Kafka*

## Install Strimzi

Install the operator:
```sh
kubectl create ns kafka
kubectl create -f operator.crd.yml -n kafka
```

Then, install the Kafka cluster using a single-node K-Raft:
```sh
kubectl apply -f kafka-cluster.yml -n kafka
kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n kafka
```

## Make it work

Create the CSV documents by hand, using this command:
```sh
kubectl -n kafka run kafka-producer -ti --image=quay.io/strimzi/kafka:0.45.0-kafka-3.9.0 --rm=true --restart=Never -- bin/kafka-console-producer.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 --topic streaming.test
```

The expected CSV is the users CSV that was previously used:
```csv
1,P1,28,New York
2,P2,22,San Francisco
3,P3,30,New York
4,P4,25,Boston
5,P5,35,San Francisco
```

You just need to output any line from that file
