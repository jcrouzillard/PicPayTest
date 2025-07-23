#!/bin/sh

curl -X POST "${CONNECT_URL}/connectors" \
  -H "Content-Type: application/json" \
  --data '{
              "name": "picpay-connector",
              "config": {
                "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
                "plugin.name": "pgoutput",
                "database.hostname": "postgres-dev",
                "database.port": "5432",
                "database.user": "picpay",
                "database.password": "picpay",
                "database.dbname": "picpay_dev",
                "database.server.name": "picpayserver",
                "topic.prefix": "picpay_pg",
                "slot.name": "picpayslot",
                "table.include.list": "public.transaction",
                "tombstones.on.delete": "false",
                "key.converter.schemas.enable": "false",
                "value.converter.schemas.enable": "false"
              }
            }'