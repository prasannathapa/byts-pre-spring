#!/usr/bin/env bash
# Run one demo without an IDE:   ./run.sh Demo01HelloDatabase
# With no argument it runs the first one.
cd "$(dirname "$0")"
CLASS="${1:-Demo01HelloDatabase}"
mkdir -p out
javac -cp "jars/*" -d out src/*.java || exit 1
java -cp "jars/*:out" "$CLASS"
