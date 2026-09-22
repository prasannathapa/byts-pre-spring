#!/usr/bin/env bash
# Run all three parts of the Day 1 capstone in order. Requires Java 25 (JDK).
# Exits non-zero while any part still has a failing test, so CI can gate on it.
cd "$(dirname "$0")"

fails=0

run_part () {
  echo ""
  echo "======================================================================"
  echo "  $1"
  echo "======================================================================"
  ( cd "$2" && bash run.sh ) || fails=$((fails + 1))
}

run_part "PART 1 - a tiny bank (the four pillars)"        part1-bank
run_part "PART 2 - a report exporter (the five principles)" part2-report
run_part "FINALE - export a bank statement (pillars + SOLID)" part3-finale

echo ""
if [ "$fails" -gt 0 ]; then
  echo "$fails of 3 parts are not green yet. Those failures are your to-do list."
  exit 1
fi
echo "All three parts green. That is the whole of Day 1 in working code."
