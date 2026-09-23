package finale;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * GIVEN, and complete. The worked example: read it, then write the JDBC one.
 *
 * A LinkedHashMap and a counter. It is already green, so every failure you see when
 * you run the tests belongs to JdbcAccountRepository. Compare the two when you are
 * done: same six methods, same contract, wildly different amount of machinery, and
 * the service upstairs cannot tell them apart.
 */
public final class InMemoryAccountRepository implements AccountRepository {

    private final Map<Long, Account> rows = new LinkedHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public Account save(Account account) {
        Account saved = account.withId(nextId.getAndIncrement());
        rows.put(saved.id(), saved);
        return saved;
    }

    @Override
    public List<Account> saveAll(List<Account> accounts) {
        List<Account> out = new ArrayList<>(accounts.size());
        for (Account a : accounts) out.add(save(a));
        return out;
    }

    @Override
    public Optional<Account> findById(long id) {
        return Optional.ofNullable(rows.get(id));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(rows.values());
    }

    @Override
    public long count() {
        return rows.size();
    }

    @Override
    public void deleteAll() {
        rows.clear();
        nextId.set(1);
    }
}
