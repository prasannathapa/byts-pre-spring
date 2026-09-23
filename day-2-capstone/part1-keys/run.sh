#!/usr/bin/env bash
# Compile part 1 and run the tests. Requires Java 25 (JDK).
cd "$(dirname "$0")"
javac keys/*.java && java keys.RunTests
