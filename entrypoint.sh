#!/bin/sh

# Inicia o Kafka Connect em segundo plano
echo "Iniciando Kafka Connect..."
/kafka/bin/connect-distributed.sh /kafka/config/connect-log4j.properties &

# Captura o PID do processo
KAFKA_CONNECT_PID=$!

# Aguarda Kafka Connect ficar disponível
echo "Esperando Kafka Connect ficar disponível em http://localhost:8083..."
until curl -s http://localhost:8083/ | grep -q "version"; do
  echo "Kafka Connect ainda não está pronto..."
  sleep 5
done

echo "Kafka Connect está disponível. Registrando conector..."
sh /register-picpay-connector.sh

# Aguarda o processo principal
wait "$KAFKA_CONNECT_PID"