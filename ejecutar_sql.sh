#!/bin/bash

while ! mysqladmin ping --silent; do
    sleep 1
done

for f in /docker-entrypoint-initdb.d/*.sql; do
    echo "Ejecutando $f"
    mysql -u root -prenaido spotify_client < "$f"
done
