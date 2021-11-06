#! /bin/bash
./wait-for-it.sh cassandra-db:9042 -t 15
./wait-for-it.sh mongo-db:27017 -t 15
java -jar app.jar -Dspring.config.location=application.yml
