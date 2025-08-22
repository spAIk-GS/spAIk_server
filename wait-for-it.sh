#!/usr/bin/env bash
# Use: ./wait-for-it.sh host:port -- command args
# Reference: https://github.com/vishnubob/wait-for-it

set -e

hostport=$1
shift
cmd="$@"

host=$(echo $hostport | cut -d: -f1)
port=$(echo $hostport | cut -d: -f2)

echo "Waiting for $host:$port..."

while ! nc -z $host $port; do
  sleep 1
done

echo "$host:$port is available, starting command..."
exec $cmd
