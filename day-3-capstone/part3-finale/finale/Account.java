package finale;

/**
 * An account that may or may not have been saved yet. GIVEN.
 *
 * The id is a Long rather than a long for one reason: before the database has seen
 * this row there IS no id, and null says that out loud. A 0 would be a lie you have
 * to remember. of() builds an unsaved account; withId() returns a new one carrying
 * the id the database handed back, because a record never changes.
 */
public record Account(Long id, String owner, long balanceCents) {

    public static Account of(String owner, long balanceCents) {
        return new Account(null, owner, balanceCents);
    }

    public Account withId(long id) {
        return new Account(id, owner, balanceCents);
    }
}
