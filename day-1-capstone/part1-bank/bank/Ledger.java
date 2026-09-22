package bank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Holds accounts and runs transactions over them.  (pillar: composition)
 *
 * Note the design: the ledger HOLDS a map as a private field. It does NOT extend a
 * map. That is composition over inheritance: it exposes only open / account / run,
 * not the hundred methods of Map. Implement the TODOs.
 */
public final class Ledger {

    private final Map<String, Account> accounts = new HashMap<>();

    public void open(Account account) {
        // TODO: store the account, keyed by its id.
        throw new UnsupportedOperationException("TODO: open(account)");
    }

    public Account account(String id) {
        // TODO: return the account with this id, or throw
        //       NoSuchElementException if there is no such account.
        throw new UnsupportedOperationException("TODO: account(id)");
    }

    public void run(Transaction t) {
        // TODO: let the transaction apply itself (this is the polymorphic call).
        throw new UnsupportedOperationException("TODO: run(t)");
    }

    public void runAll(List<Transaction> transactions) {
        // TODO: run each transaction, in order.
        throw new UnsupportedOperationException("TODO: runAll(transactions)");
    }
}
