#!/usr/bin/env bash
# Compile this part and run the tests. Requires Java 25 (JDK) and the jars in ../lib (run ../get-deps.sh once).
cd "$(dirname "$0")"
ls ../lib/*.jar >/dev/null 2>&1 || { echo "No jars in lib/ yet - run ./get-deps.sh in the capstone folder first."; exit 1; }
javac -cp "../lib/*" transfer/*.java && java -cp "../lib/*:." transfer.RunTests
