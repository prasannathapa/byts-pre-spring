package keys;

/**
 * A version number, major.minor.patch, used as a MAP KEY and a SET ELEMENT.
 * (Effective Java Items 10, 11, 14 and 17.)
 *
 * This is deliberately NOT a record. A record would hand you equals and hashCode for
 * free, and writing them yourself is half the point of this part. The fields are final
 * and there is no setter: once built, a Version never changes, which is exactly what a
 * key has to promise. Mutate a key inside a HashSet and you lose it.
 *
 * Three contracts to honour, and the collections below will punish you if you don't:
 *   equals      reflexive, symmetric, transitive, consistent, and false for null
 *   hashCode    equal objects MUST return the same int
 *   compareTo   consistent with equals: compareTo(x) == 0 exactly when equals(x)
 */
public final class Version implements Comparable<Version> {

    private final int major;
    private final int minor;
    private final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Parse "1.10.0" into a Version.
     *
     * Reject anything that is not exactly three runs of digits separated by two dots.
     * Null counts as bad input, and so does the empty string: check BOTH, not just the
     * one your eye lands on. Throw IllegalArgumentException for every bad shape.
     */
    public static Version parse(String text) {
        // TODO: reject null and blank text, split on '.', demand exactly three parts,
        //       demand every part is digits only, then build the Version.
        throw new UnsupportedOperationException("TODO: Version.parse(text)");
    }

    public int major() {
        throw new UnsupportedOperationException("TODO: major()");
    }

    public int minor() {
        throw new UnsupportedOperationException("TODO: minor()");
    }

    public int patch() {
        throw new UnsupportedOperationException("TODO: patch()");
    }

    @Override
    public boolean equals(Object other) {
        // TODO: same class, same three numbers. Nothing else is equal to a Version,
        //       and null never is.
        throw new UnsupportedOperationException("TODO: equals(other)");
    }

    @Override
    public int hashCode() {
        // TODO: derive it from the same three fields equals uses. Objects.hash is fine.
        throw new UnsupportedOperationException("TODO: hashCode()");
    }

    /**
     * Order NUMERICALLY: major first, then minor, then patch.
     * 1.9.0 comes before 1.10.0. Compare the strings instead and you get that backwards,
     * because '9' is bigger than '1'. That is the bug this method exists to avoid.
     */
    @Override
    public int compareTo(Version other) {
        throw new UnsupportedOperationException("TODO: compareTo(other)");
    }

    /** "1.10.0" again, so parse(v.toString()).equals(v). */
    @Override
    public String toString() {
        throw new UnsupportedOperationException("TODO: toString()");
    }
}
