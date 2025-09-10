#!/bin/bash

cd payment
./mvnw clean install
docker build -t payment .
cd ..
docker compose up --force-recreate