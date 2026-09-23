package finale;

import java.util.List;
import java.util.Optional;

/**
 * Storage, described without saying where. GIVEN, and you do not edit it.
 *
 * Nothing in this interface mentions SQL, connections or H2. That is the point: the
 * service above it depends on THIS, so it works the same against a HashMap and
 * against a database, and the tests prove it by running one set of checks against
 * both. Day 1 called that dependency inversion. Spring Data will hand you an
 * interface exactly this shape and write the implementation itself.
 */
public interface AccountRepository {

    /** Saves a new account and returns it with its id filled in. */
    Account save(Account account);

    /** Saves many accounts and returns them with their ids, in the same order. */
    List<Account> saveAll(List<Account> accounts);

    Optional<Account> findById(long id);

    List<Account> findAll();

    long count();

    void deleteAll();
}
