#!/usr/bin/env bash
# Compile part 2 and run the tests. Requires Java 25 (JDK).
cd "$(dirname "$0")"
javac wrappers/*.java && java wrappers.RunTests
