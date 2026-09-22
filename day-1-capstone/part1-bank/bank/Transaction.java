package bank;

/**
 * A banking action that knows how to apply itself.  (pillars: abstraction + polymorphism)
 *
 * This is a SEALED family: these three are the only kinds of transaction that can
 * ever exist, and the compiler enforces it. The interface is given to you. Your job
 * is to implement each record's apply(...) so the ledger can run any transaction
 * through one call, without ever checking its type.
 */
public sealed interface Transaction permits Deposit, Withdraw, Transfer {
    void apply(Ledger ledger);
}

record Deposit(String accountId, long cents) implements Transaction {
    public void apply(Ledger ledger) {
        // TODO: deposit `cents` into the account with this id.
        throw new UnsupportedOperationException("TODO: Deposit.apply");
    }
}

record Withdraw(String accountId, long cents) implements Transaction {
    public void apply(Ledger ledger) {
        // TODO: withdraw `cents` from the account with this id.
        throw new UnsupportedOperationException("TODO: Withdraw.apply");
    }
}

record Transfer(String fromId, String toId, long cents) implements Transaction {
    public void apply(Ledger ledger) {
        // TODO: withdraw from `fromId`, then deposit into `toId`.
        //       (If the withdrawal fails, no money should move.)
        throw new UnsupportedOperationException("TODO: Transfer.apply");
    }
}
