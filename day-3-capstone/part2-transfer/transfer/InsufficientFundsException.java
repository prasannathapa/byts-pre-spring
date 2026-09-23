package transfer;

/**
 * GIVEN. Thrown when a transfer would push an account below zero.
 *
 * Unchecked on purpose. The database reports the same thing as a
 * SQLIntegrityConstraintViolationException with an SQLState in class 23, which is a
 * checked exception full of vendor detail. Callers of a transfer service should not
 * have to import java.sql to understand "not enough money". Translating at the
 * boundary is what Spring's DataAccessException does, and you are doing it by hand.
 */
public class InsufficientFundsException extends RuntimeException {

    private final long from, to, cents;

    public InsufficientFundsException(long from, long to, long cents) {
        super("account " + from + " cannot send " + cents + " cents to account " + to);
        this.from = from;
        this.to = to;
        this.cents = cents;
    }

    public long from()  { return from; }
    public long to()    { return to; }
    public long cents() { return cents; }
}
