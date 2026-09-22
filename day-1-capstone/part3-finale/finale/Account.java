package finale;

/**
 * A bank account with a guarded balance.  (pillar: ENCAPSULATION)
 *
 * GIVEN - you do not edit this. It is the same idea you built in part 1: the
 * balance is private, and the only way to move it is through deposit / withdraw,
 * which enforce the rules. The finale reuses this shape and exports it.
 *
 * Same exception convention as part 1: every bad amount, and an overdraft too,
 * throws IllegalArgumentException.
 */
public final class Account {

    private final String id;
    private long balanceCents;

    public Account(String id, long openingCents) {
        if (openingCents < 0) throw new IllegalArgumentException("opening balance must not be negative");
        this.id = id;
        this.balanceCents = openingCents;
    }

    public String id() { return id; }

    public long balanceCents() { return balanceCents; }

    public void deposit(long cents) {
        if (cents <= 0) throw new IllegalArgumentException("deposit must be positive");
        balanceCents += cents;
    }

    public void withdraw(long cents) {
        if (cents <= 0) throw new IllegalArgumentException("withdraw must be positive");
        if (cents > balanceCents) throw new IllegalArgumentException("insufficient funds");
        balanceCents -= cents;
    }
}
