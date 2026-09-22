#!/usr/bin/env bash
# Compile the capstone and run the tests. Requires Java 25 (JDK).
cd "$(dirname "$0")"
javac bank/*.java && java bank.RunTests
