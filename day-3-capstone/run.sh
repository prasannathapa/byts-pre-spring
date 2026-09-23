#!/usr/bin/env bash
# Run all three parts of the Day 3 capstone in order. Requires Java 25 (JDK).
# Fetches the H2 jar first if lib/ is empty.
# Exits non-zero while any part still has a failing test, so CI can gate on it.
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

run_part "PART 1 - connect, create, insert, query"                  part1-connect
run_part "PART 2 - transfer money, and survive hostile input"       part2-transfer
run_part "FINALE - one repository interface, two implementations"   part3-finale

echo ""
if [ "$fails" -gt 0 ]; then
  echo "$fails of 3 parts are not green yet. Those failures are your to-do list."
  exit 1
fi
echo "All three parts green. That is the whole of Day 3 in working code."
