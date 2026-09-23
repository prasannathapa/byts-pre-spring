package finale;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Given, because this is Day 3 material and the finale is long enough already. Read it:
 * it is the layer that owns the rules, and it holds a repository it did not create.
 *
 * Three refusals, three different exceptions, and not one HTTP status in sight. The service
 * has no idea it is behind a servlet today.
 */
public class AccountService {

    private final AccountRepository accounts;

    public AccountService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    public Optional<Account> find(long id) {
        return accounts.findById(id);
    }

    public List<Account> all() {
        return accounts.findAll();
    }

    public void transfer(long fromId, long toId, long amountPaise) {
        if (amountPaise <= 0) throw new IllegalArgumentException("amount must be positive: " + amountPaise);
        if (fromId == toId) throw new IllegalArgumentException("an account cannot pay itself");
        accounts.findById(fromId).orElseThrow(() -> new NoSuchElementException("no account " + fromId));
        accounts.findById(toId).orElseThrow(() -> new NoSuchElementException("no account " + toId));
        accounts.transfer(fromId, toId, amountPaise);      // the repository owns the transaction
    }
}
