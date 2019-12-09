#!/usr/bin/env bash

port=$1

if ( nc -zv localhost $port 2>&1 >/dev/null ); then
  java -jar web/target/web-1.0-SNAPSHOT.jar --server.port=0
else
  java -jar web/target/web-1.0-SNAPSHOT.jar --server.port=$port
fi