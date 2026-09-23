package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The one place in this package that knows where the database lives.
 *
 * H2 in memory: nothing to install, nothing left on disk. DB_CLOSE_DELAY=-1 tells
 * H2 to keep the database alive even when the last connection closes, which is what
 * lets one test close its connection and the next test still find the table.
 *
 * Notice what is NOT here: no Class.forName, no driver class name anywhere. The h2
 * jar carries META-INF/services/java.sql.Driver, so DriverManager finds the driver
 * through ServiceLoader the first time you ask for a connection. One test in
 * RunTests reads this package's source and fails if it finds Class.forName, so
 * resist the temptation to paste it in from an old tutorial.
 */
public final class Db {

    public static final String URL = "jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1";

    private Db() { }

    public static Connection open() throws SQLException {
        // TODO: hand back a Connection for URL. This is one line, and DriverManager
        //       does the driver hunting for you. Do not swallow the SQLException:
        //       the caller declared it too, so let it out.
        throw new UnsupportedOperationException("TODO: Db.open()");
    }
}
