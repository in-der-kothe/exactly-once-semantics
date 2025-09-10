#!/bin/bash

cd payment
./mvnw clean install
podman build -t payment .
cd ..
podman-compose up --force-recreate