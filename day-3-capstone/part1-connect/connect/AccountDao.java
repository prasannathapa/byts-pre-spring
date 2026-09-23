package connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Rows in, objects out. Three queries and one mapper.
 *
 * Every value that came from a caller is BOUND with a ?, never glued into the SQL
 * with +. That is not a style rule. It is the difference between a name like
 * O'Brien working and your table being dropped by a customer.
 *
 * All three reads finish in the same place: toAccount. Write the mapping once and
 * every query gets it. Keep that shape in your head, because tomorrow it has a name:
 * Spring's JdbcTemplate calls it a RowMapper and takes it as a parameter.
 */
public final class AccountDao {

    private static final String INSERT    = "INSERT INTO account(owner, balance_cents) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT id, owner, balance_cents FROM account WHERE id = ?";
    private static final String FIND_ALL  = "SELECT id, owner, balance_cents FROM account ORDER BY id";

    private AccountDao() { }

    /** Inserts a row and returns the id the database generated for it. */
    public static long insert(Connection c, String owner, long cents) throws SQLException {
        // TODO: prepare INSERT with Statement.RETURN_GENERATED_KEYS, bind the two values
        //       (setString, setLong - the index starts at 1, not 0), executeUpdate, then
        //       read the id out of getGeneratedKeys(). next() before getLong(1), always.
        //       If there is no generated key, throw a SQLException saying so rather than
        //       returning 0 and leaving the caller to guess.
        throw new UnsupportedOperationException("TODO: AccountDao.insert(connection, owner, cents)");
    }

    /** The account with this id, or an empty Optional if there is no such row. */
    public static Optional<Account> findById(Connection c, long id) throws SQLException {
        // TODO: prepare FIND_BY_ID, bind the id, execute, and return
        //       rs.next() ? Optional.of(toAccount(rs)) : Optional.empty().
        //       A missing row is not an error and it is not null. That is the whole
        //       point of Optional: the signature says out loud that there may be nothing.
        throw new UnsupportedOperationException("TODO: AccountDao.findById(connection, id)");
    }

    /** Every account, ordered by id. Empty list when the table is empty. */
    public static List<Account> findAll(Connection c) throws SQLException {
        // TODO: execute FIND_ALL and loop while (rs.next()), adding toAccount(rs) to a list.
        //       One next() per row. Call it twice in the loop and you silently skip rows.
        throw new UnsupportedOperationException("TODO: AccountDao.findAll(connection)");
    }

    /**
     * The one mapper. The cursor is already sitting on a row when this is called;
     * this method never calls next().
     */
    private static Account toAccount(ResultSet rs) throws SQLException {
        // TODO: build an Account from the three columns. Read them by LABEL
        //       ("balance_cents"), which is what the query called them.
        throw new UnsupportedOperationException("TODO: AccountDao.toAccount(resultSet)");
    }
}
