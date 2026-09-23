#!/usr/bin/env bash
# Compile the finale and run the tests. Requires Java 25 (JDK).
cd "$(dirname "$0")"
javac finale/*.java && java finale.RunTests
