#!/bin/bash

cd media
mvn clean install -B

cd ../registry
mvn clean install -B

cd ../ui
mvn clean install -B