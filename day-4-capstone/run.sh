#!/usr/bin/env bash
# Run all three parts of the Day 4 capstone in order. Requires Java 25 (JDK).
# Fetches the jars first if lib/ is empty, then exits non-zero while any part still fails.
cd "$(dirname "$0")"

ls lib/*.jar >/dev/null 2>&1 || bash get-deps.sh || exit 1

fails=0

run_part () {
  echo ""
  echo "======================================================================"
  echo "  $1"
  echo "======================================================================"
  ( cd "$2" && bash run.sh ) || fails=$((fails + 1))
}

run_part "PART 1 - boot Tomcat, say hello, speak JSON"                 part1-hello
run_part "PART 2 - a filter, a listener, a session, fifty threads"     part2-state
run_part "FINALE - App.java, the composition root of a bank over HTTP" part3-finale

echo ""
if [ "$fails" -gt 0 ]; then
  echo "$fails of 3 parts are not green yet. Those failures are your to-do list."
  exit 1
fi
echo "All three parts green. You wrote the container's application, and then you wired it."
