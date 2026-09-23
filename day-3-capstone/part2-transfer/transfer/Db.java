package transfer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * GIVEN. The database, the schema and three customers to move money between.
 *
 * Ids are fixed here rather than generated, so the tests can name accounts. The
 * CHECK on balance_cents is the whole reason part 2 works: the database itself
 * refuses to hold a negative balance, so an overdraft fails even if your Java forgot
 * to look. Never rely on that alone in real code, but never leave it out either.
 */
public final class Db {

    public static final String URL = "jdbc:h2:mem:transfer;DB_CLOSE_DELAY=-1";

    public static final long ANA = 1, BO = 2, OBRIEN = 3;

    private Db() { }

    public static Connection open() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Drops everything, rebuilds the table and seeds the three accounts. */
    public static void reset(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) {
            s.execute("DROP TABLE IF EXISTS account");
            s.execute("""
                    CREATE TABLE account (
                        id            BIGINT      PRIMARY KEY,
                        owner         VARCHAR(60) NOT NULL,
                        balance_cents BIGINT      NOT NULL CHECK (balance_cents >= 0)
                    )""");
        }
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO account VALUES (?, ?, ?)")) {
            seed(ps, ANA, "ana", 10_000);
            seed(ps, BO, "bo", 5_000);
            seed(ps, OBRIEN, "O'Brien", 700);
        }
    }

    private static void seed(PreparedStatement ps, long id, String owner, long cents) throws SQLException {
        ps.setLong(1, id);
        ps.setString(2, owner);
        ps.setLong(3, cents);
        ps.executeUpdate();
    }

    /** The balance of one account, for the tests to check your arithmetic. */
    public static long balance(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT balance_cents FROM account WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("no account with id " + id);
                return rs.getLong(1);
            }
        }
    }
}
