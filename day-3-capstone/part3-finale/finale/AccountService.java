package finale;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * GIVEN, and never edited. The policy: totals and the biggest balance.
 *
 * Look at what it does not know. No URL, no Connection, no SQL, no idea whether
 * findAll costs a nanosecond or a network round trip. It was handed an
 * AccountRepository in its constructor and that is the end of its curiosity. The
 * tests run this same class against the HashMap repository and the JDBC one and
 * expect identical answers.
 */
public final class AccountService {

    private final AccountRepository accounts;

    public AccountService(AccountRepository accounts) {
        if (accounts == null) throw new IllegalArgumentException("a service needs a repository");
        this.accounts = accounts;
    }

    public long totalBalance() {
        long total = 0;
        for (Account a : accounts.findAll()) total += a.balanceCents();
        return total;
    }

    public Optional<Account> richest() {
        List<Account> all = accounts.findAll();
        return all.stream().max(Comparator.comparingLong(Account::balanceCents));
    }
}
