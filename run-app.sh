#!/usr/bin/env bash

if [ -z "$1" ]
thengit
  java -jar web/target/web-1.0-SNAPSHOT.jar
else
  java -jar web/target/web-1.0-SNAPSHOT.jar --server.port=$1
fi
