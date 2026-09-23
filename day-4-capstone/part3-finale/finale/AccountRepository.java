package finale;

import java.util.List;
import java.util.Optional;

/**
 * Given. The seam from Day 1, wearing Day 3's clothes.
 *
 * Nothing above this interface knows there is a database. findById returns Optional because
 * "no such account" is an ordinary answer, not a null to trip over later.
 */
public interface AccountRepository {

    Optional<Account> findById(long id);

    List<Account> findAll();

    Account save(Account account);

    /** Moves money in ONE transaction: both rows change or neither does. */
    void transfer(long fromId, long toId, long amountPaise);
}
