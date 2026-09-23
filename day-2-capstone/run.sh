#!/usr/bin/env bash
# Run all three parts of the Day 2 capstone in order. Requires Java 25 (JDK).
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

run_part "PART 1 - a Version that can be a key (equals, hashCode, compareTo)" part1-keys
run_part "PART 2 - wrap a Set, and build an LRU (composition over inheritance)" part2-wrappers
run_part "FINALE - WordStats (merge, computeIfAbsent, a heap, two read-only maps)" part3-finale

echo ""
if [ "$fails" -gt 0 ]; then
  echo "$fails of 3 parts are not green yet. Those failures are your to-do list."
  exit 1
fi
echo "All three parts green. That is the Collections framework used the way it was meant to be."
