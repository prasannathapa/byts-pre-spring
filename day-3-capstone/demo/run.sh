#!/usr/bin/env bash
# Run the lost-update demo. Requires Java 25 (JDK) and the jar in lib (run ./get-deps.sh once).
# No argument: watch a deposit get lost. Or pass atomic, lock or version to see it fixed.
cd "$(dirname "$0")/.."
ls lib/*.jar >/dev/null 2>&1 || { echo "No jars in lib/ yet - run ./get-deps.sh first."; exit 1; }
javac -cp "lib/*" demo/LostUpdate.java && java -cp "lib/*:." demo.LostUpdate "$@"
