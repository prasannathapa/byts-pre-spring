package finale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

/**
 * The one class you write in the finale. About ninety lines.
 *
 * It takes a DataSource, not a URL and not a Connection. A DataSource is the seam
 * that lets somebody drop HikariCP in front of this class without touching a line of
 * it, and it is the reason the constructor can be dull: store the reference, do no
 * I/O. A repository that connects in its constructor cannot be built at startup when
 * the database is still coming up, and one test checks exactly that.
 *
 * Every method borrows a connection, uses it, and gives it back through
 * try-with-resources. A borrowed connection you forget to close is not a leak you
 * notice in a test; it is a leak you notice when the pool runs dry at 4pm.
 *
 * Every SQLException becomes a RepositoryException with the cause attached. Callers
 * upstairs get one unchecked type; you get the stack trace when it goes wrong.
 */
public final class JdbcAccountRepository implements AccountRepository {

    private static final String DDL = """
            CREATE TABLE IF NOT EXISTS account (
                id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                owner         VARCHAR(60) NOT NULL,
                balance_cents BIGINT      NOT NULL CHECK (balance_cents >= 0)
            )""";

    private static final String INSERT     = "INSERT INTO account(owner, balance_cents) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT id, owner, balance_cents FROM account WHERE id = ?";
    private static final String FIND_ALL   = "SELECT id, owner, balance_cents FROM account ORDER BY id";
    private static final String COUNT      = "SELECT count(*) FROM account";

    // TODO: one field, the injected DataSource. Nothing else.

    public JdbcAccountRepository(DataSource dataSource) {
        // TODO: store it in a field, and reject null while you are here.
        //       Do NOT open a connection and do NOT create the schema in here. One test
        //       builds a repository over a DataSource that points at nothing and expects
        //       this constructor to succeed anyway, because a repository is built at
        //       startup, when the database may still be waking up.
        //       Left empty rather than throwing, so the rest of the tests can run and
        //       show you a list of red lines instead of one stack trace.
    }

    /** Creates the table. Called by the tests, not by the constructor. */
    public void createSchema() {
        // TODO: borrow a connection, run DDL through a Statement, wrap any SQLException.
        throw new UnsupportedOperationException("TODO: createSchema()");
    }

    @Override
    public Account save(Account account) {
        // TODO: prepare INSERT with the key column NAMED: prepareStatement(INSERT, new String[]{"id"}).
        //       Bind owner and balance, executeUpdate, read the id from getGeneratedKeys(),
        //       and return account.withId(thatId). Return the copy, not the argument.
        throw new UnsupportedOperationException("TODO: save(account)");
    }

    @Override
    public List<Account> saveAll(List<Account> accounts) {
        // TODO: one connection, one transaction, one batch.
        //       setAutoCommit(false); addBatch() per account; executeBatch();
        //       then read getGeneratedKeys() ONCE, in order, pairing each key with the
        //       account it belongs to; commit. On any failure: rollback, then throw
        //       RepositoryException with the SQLException as the cause. Reset autocommit
        //       in a finally. A batch where row 2 is illegal must leave the table exactly
        //       as it was, which only happens if the transaction wraps the whole batch.
        throw new UnsupportedOperationException("TODO: saveAll(accounts)");
    }

    @Override
    public Optional<Account> findById(long id) {
        // TODO: FIND_BY_ID, bound id, Optional.of(toAccount(rs)) or Optional.empty().
        throw new UnsupportedOperationException("TODO: findById(id)");
    }

    @Override
    public List<Account> findAll() {
        // TODO: FIND_ALL, loop, map every row through toAccount.
        throw new UnsupportedOperationException("TODO: findAll()");
    }

    @Override
    public long count() {
        // TODO: COUNT. One row, one column, and you still have to call next() first.
        throw new UnsupportedOperationException("TODO: count()");
    }

    @Override
    public void deleteAll() {
        // TODO: DELETE FROM account. The tests call this between runs.
        throw new UnsupportedOperationException("TODO: deleteAll()");
    }

    /** The one mapper. The cursor is already on a row; this never calls next(). */
    private static Account toAccount(ResultSet rs) throws SQLException {
        return new Account(rs.getLong("id"), rs.getString("owner"), rs.getLong("balance_cents"));
    }
}
