CQL="CREATE KEYSPACE IF NOT EXISTS openklaster WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'} AND durable_writes = 'true';
CREATE TABLE IF NOT EXISTS openklaster.loadmeasurement ( timestamp timestamp PRIMARY KEY, installationId text, unit text, value double);
CREATE TABLE IF NOT EXISTS openklaster.sourcemeasurement ( timestamp timestamp PRIMARY KEY, installationId text, unit text, value double);
CREATE TABLE IF NOT EXISTS openklaster.weatherconditions ( timestamp timestamp PRIMARY KEY, installationId text, source text, type text, description text);
CREATE TABLE IF NOT EXISTS openklaster.energypredictions ( timestamp timestamp PRIMARY KEY, installationId text, source text, type text, description text);"

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 10
done &

exec /docker-entrypoint.sh "$@"