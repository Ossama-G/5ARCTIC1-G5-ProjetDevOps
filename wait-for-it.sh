#!/bin/sh

echo "Waiting for MySQL to be ready..."
while ! nc -z database 3306; do
  sleep 1
done
echo "MySQL is ready!"
exec java -jar gestion-station-ski-1.0-SNAPSHOT.jar