#!/bin/bash

set -e

host="$1"
port="$2"

until nc -z -v -w30 "$host" "$port"; do
  >&2 echo "Database is unavailable - sleeping"
  sleep 1
done

>&2 echo "Database is up - starting backend"
exec "$@"
