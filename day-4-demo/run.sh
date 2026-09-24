#!/usr/bin/env bash
# Run one demo without an IDE:   ./run.sh Demo00NoFrameworkAtAll
# With no argument it runs the first one.
cd "$(dirname "$0")"
CLASS="${1:-Demo00NoFrameworkAtAll}"
mkdir -p out
javac -cp "jars/*" -d out src/*.java || exit 1
java -cp "jars/*:out" "$CLASS"
