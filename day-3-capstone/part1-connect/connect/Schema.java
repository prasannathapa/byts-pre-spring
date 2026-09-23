package connect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creating the table, and asking the database whether it is already there.
 *
 * Two different tools, on purpose. DDL is a plain Statement: there are no values to
 * bind, so there is nothing for a PreparedStatement to do. Asking what exists is
 * DatabaseMetaData, not a SELECT against some vendor table, because getTables is the
 * same call on H2, MySQL and Oracle.
 */
public final class Schema {

    static final String DDL = """
            CREATE TABLE IF NOT EXISTS account (
                id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                owner         VARCHAR(60) NOT NULL,
                balance_cents BIGINT      NOT NULL CHECK (balance_cents >= 0)
            )""";

    private Schema() { }

    public static void create(Connection c) throws SQLException {
        // TODO: run DDL through a Statement. Use try-with-resources so the statement
        //       closes even if the execute throws. IF NOT EXISTS is already in the DDL,
        //       which is why calling this twice has to be harmless.
        throw new UnsupportedOperationException("TODO: Schema.create(connection)");
    }

    public static boolean exists(Connection c) throws SQLException {
        // TODO: ask c.getMetaData().getTables(null, null, "ACCOUNT", new String[]{"TABLE"})
        //       and answer whether it found a row. Close the ResultSet.
        //       Why "ACCOUNT" in capitals: we wrote the table name unquoted, and an
        //       unquoted identifier is folded to upper case, so that is the name the
        //       catalogue holds. Ask for "account" and you get nothing back.
        throw new UnsupportedOperationException("TODO: Schema.exists(connection)");
    }

    /**
     * GIVEN. Drops the table so a test can start from an empty database.
     * There is nothing new for you in here, and the tests lean on it heavily.
     */
    public static void reset(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) {
            s.execute("DROP TABLE IF EXISTS account");
        }
    }
}
