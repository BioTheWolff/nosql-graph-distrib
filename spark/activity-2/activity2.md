
# Activity 2
Run the quick start program on Kubernetes using Spark Operator

## Installing Spark Operator

Installing the operator:
```sh
# Add the Helm repository
helm repo add spark-operator https://kubeflow.github.io/spark-operator
helm repo update

# Install the operator into the spark-operator namespace and wait for deployments to be ready
helm install spark-operator spark-operator/spark-operator \
    --namespace spark-operator --create-namespace --wait
```

Then run the example application:
```sh
# Create an example application in the default namespace
kubectl apply -f https://raw.githubusercontent.com/kubeflow/spark-operator/refs/heads/master/examples/spark-pi.yaml

# Get the status of the application
kubectl get sparkapp spark-pi
```

## Installing MinIO

```sh
helm repo add minio-operator https://operator.min.io
helm install \
  --namespace minio-operator \
  --create-namespace \
  operator minio-operator/operator
```

Then creating the tenant:
```sh
helm install \
--namespace s3 \
--create-namespace \
--values minio-values.yaml \
s3 minio-operator/tenant
kubectl port-forward svc/myminio-hl 9000 -n s3
```

Setting up the tenant (assuming usage of MinIO client):
```sh
mc alias set myminio http://localhost:9000 minio minio123 --insecure
mc mb myminio/mybucket --insecure
mcli anonymous set download myminio/mybucket --insecure
```

## Running the program

The code for this activity's JAR can be found [here](../Spark/src/main/scala/Activity2.scala).
You need to build the JAR and place it in the data folder along the CSV.

Copy the files to the bucket:
```sh
mc cp data/users.csv myminio/mybucket --insecure
mc cp data/activity2-1.0.0.jar myminio/mybucket --insecure
```

Then execute the job:
```sh
kubectl apply -f job.yml
```