package keys;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A sorted registry of Versions. It HOLDS a TreeSet, it does not extend one.
 *
 * Two ideas share this class. The first is that a TreeSet is only as good as the
 * compareTo you gave it: fix Version.compareTo and this whole class comes alive.
 * The second is that a method can take BEHAVIOUR as a parameter. matching takes a
 * Predicate, latestOrElse takes a Supplier, and neither one knows or cares what the
 * caller put inside the lambda. That is the same dependency inversion you did on
 * day 1, shrunk down to one argument.
 */
public final class Registry {

    private final TreeSet<Version> versions = new TreeSet<>();

    public void add(Version v) {
        throw new UnsupportedOperationException("TODO: add(v)");
    }

    public boolean contains(Version v) {
        throw new UnsupportedOperationException("TODO: contains(v)");
    }

    /** The highest version, or an empty Optional when nothing has been added. Never null. */
    public Optional<Version> latest() {
        // TODO: TreeSet.last() throws on an empty set, so check first.
        throw new UnsupportedOperationException("TODO: latest()");
    }

    /**
     * Every version, ascending, as an UNMODIFIABLE VIEW of the live set.
     * A view, not a copy: later adds show through it. Handing back the TreeSet itself
     * would let a caller add to your private state, and Item 15 says do not.
     */
    public SortedSet<Version> all() {
        throw new UnsupportedOperationException("TODO: all()");
    }

    /**
     * The versions the predicate accepts, ascending, as an unmodifiable List.
     * Call the predicate. Do not guess what it probably meant and hard-code that.
     * When nothing matches, return an EMPTY list, never null (Item 54).
     */
    public List<Version> matching(Predicate<Version> test) {
        throw new UnsupportedOperationException("TODO: matching(test)");
    }

    /**
     * The latest version, or whatever the Supplier builds if there isn't one.
     *
     * Use orElseGet, not orElse. orElse evaluates its argument ALWAYS, even when the
     * Optional has a value, so orElse(expensive()) pays for the fallback you never use.
     * orElseGet only calls the Supplier when it has to (Effective Java Item 55).
     */
    public Version latestOrElse(Supplier<Version> fallback) {
        throw new UnsupportedOperationException("TODO: latestOrElse(fallback)");
    }
}
