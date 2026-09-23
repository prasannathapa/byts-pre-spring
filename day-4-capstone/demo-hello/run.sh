#!/usr/bin/env bash
# The mid-morning demo: one servlet, one main method, port 8080. Ctrl-C to stop.
cd "$(dirname "$0")"
ls ../lib/*.jar >/dev/null 2>&1 || { echo "No jars in lib/ yet - run ./get-deps.sh in the capstone folder first."; exit 1; }
javac -cp "../lib/*" demo/*.java && java -cp "../lib/*:." demo.App
