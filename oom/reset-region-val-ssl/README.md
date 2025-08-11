# Deployment Instructions

## Deploy Application

```bash
mvn -DskipTests clean package --settings $HOME/settings.xml
java -jar target/reset-region-val-ssl-0.0.1-SNAPSHOT.jar
```

## Test API Calls

```bash
# Get existing Value
curl -s "http://localhost:8080/api/get/K10"

# Create key,value
curl -s -X POST "http://localhost:8080/api/put/A8/8"

# Reset value to 0
curl -s -X POST "http://localhost:8080/api/reset/A8"

```

## OQL queries to verify data

```bash
# From gfsh
query --query="SELECT e.key, e.value FROM /oom.entrySet e"
```
