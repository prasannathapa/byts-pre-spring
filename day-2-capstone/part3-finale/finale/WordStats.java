package finale;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Counting words. Everything from today lands in this one class.
 *
 * A HashMap holds the counts. A PriorityQueue finds the top few without sorting the
 * whole vocabulary. A TreeMap groups by first letter and keeps the letters in order.
 * Two of the methods hand out the counts, one as a snapshot and one as a live view, and
 * the difference between those two is the last idea of the day.
 *
 * WORDS: lower case, and a word is a run of letters or digits. Split on everything else,
 * so "The cat, the CAT!" is four words and two of them are "cat". Ignore empty pieces.
 *
 * LOWER CASE IN WHICH LANGUAGE? toLowerCase() with no argument uses the machine's default
 * locale, and in Turkish 'I' lower-cases to a dotless i that is not the letter i at all.
 * Your code would then read INDIA as two words. Pass Locale.ROOT everywhere you fold case:
 * toLowerCase(Locale.ROOT). There is a test that sets a Turkish locale and checks.
 */
public final class WordStats {

    private final Map<String, Integer> counts = new HashMap<>();

    /** Split text into words and add them to the counts. */
    public void add(String text) {
        // TODO: lower-case the text with toLowerCase(Locale.ROOT), split on runs of
        //       non-alphanumeric characters, skip the empty pieces, and count each word
        //       with counts.merge(word, 1, Integer::sum).
        //       merge is the whole point: no containsKey, no null check, one line.
        throw new UnsupportedOperationException("TODO: add(text)");
    }

    /** How many times this word was seen. A word never seen is 0, not null, and not a crash. */
    public int count(String word) {
        // TODO: getOrDefault, on the word folded exactly the way add folds it, Locale.ROOT
        //       included. Return empty, never null (Item 54); for an int that means zero.
        throw new UnsupportedOperationException("TODO: count(word)");
    }

    /**
     * Take howMany off a word's count. If that takes it to zero or below, the word is GONE
     * from the map, not sitting there with a count of 0.
     *
     * merge does this for you: if the remapping function returns null, merge REMOVES the
     * entry. That is the one behaviour of merge everybody gets wrong in an interview.
     * A word that was never there stays never there: do not create a negative entry.
     */
    public void remove(String word, int howMany) {
        throw new UnsupportedOperationException("TODO: remove(word, howMany)");
    }

    /**
     * The most frequent word, or an empty Optional when nothing has been counted.
     * On a tie, the alphabetically first word wins. Write the Optional, do not fake it
     * with null and do not call get() without checking.
     */
    public Optional<String> mostFrequent() {
        throw new UnsupportedOperationException("TODO: mostFrequent()");
    }

    /**
     * The k most frequent words: count descending, ties alphabetical.
     *
     * Do it with a PriorityQueue of size k, not by sorting everything: that is O(n log k)
     * instead of O(n log n), and with a million words and k of 10 you feel the difference.
     *
     * The trick everybody trips on: the heap has to hold the WORST of the k you are keeping
     * at its head, so its comparator is the REVERSE of the order you want out. Reverse the
     * tie-break too, or the wrong one of two equally frequent words survives the boundary.
     * Offer each entry, and whenever the heap is bigger than k, poll. Sort what is left.
     *
     * k of zero, or k bigger than the vocabulary, must not throw.
     */
    public List<String> topK(int k) {
        throw new UnsupportedOperationException("TODO: topK(k)");
    }

    /**
     * Words grouped under their first character, with the letters in sorted order.
     * TreeMap for the sorted keys, computeIfAbsent to create each list on first use.
     * Sort each group alphabetically too.
     */
    public Map<Character, List<String>> byFirstLetter() {
        throw new UnsupportedOperationException("TODO: byFirstLetter()");
    }

    /** Every word, sorted, in a list the caller cannot modify. stream().sorted().toList(). */
    public List<String> words() {
        // TODO: stream().toList() is already unmodifiable. Collectors.toList() would not
        //       have been, and that difference is exactly what this test is checking.
        throw new UnsupportedOperationException("TODO: words()");
    }

    /**
     * A SNAPSHOT of the counts. Whatever happens to this WordStats afterwards, the map you
     * handed back keeps the numbers it had. Map.copyOf. It is immutable, so put throws.
     */
    public Map<String, Integer> frequencies() {
        throw new UnsupportedOperationException("TODO: frequencies()");
    }

    /**
     * A live VIEW of the counts. Later adds show through it, and it still refuses writes.
     * Collections.unmodifiableMap. Same read-only promise as frequencies, completely
     * different answer to "what happens next": that is the distinction to take away.
     */
    public Map<String, Integer> index() {
        throw new UnsupportedOperationException("TODO: index()");
    }
}
