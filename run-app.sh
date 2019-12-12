#!/usr/bin/env bash

if [ -z "$1" ]
then
  java -jar web/target/web-1.0-SNAPSHOT.jar
else
  java -jar web/target/web-1.0-SNAPSHOT.jar --server.port=$1
fi
