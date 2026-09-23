package finale;

/**
 * Given. A domain rule, not a database error and not an HTTP status.
 *
 * The repository throws it inside the transaction, the service lets it travel, and the servlet
 * is the only layer that knows it means 409. Each layer speaks its own language, which is why
 * you can put this whole graph behind a different front end tomorrow.
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
