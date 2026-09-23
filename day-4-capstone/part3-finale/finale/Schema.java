package finale;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Given. The table and two rows to play with: ann has 100 rupees, bob has 5. */
public final class Schema {

    private Schema() { }

    public static void create(DataSource ds) {
        try (Connection c = ds.getConnection(); Statement s = c.createStatement()) {
            s.execute("DROP TABLE IF EXISTS account");
            s.execute("""
                      CREATE TABLE account (
                        id            BIGINT PRIMARY KEY,
                        owner         VARCHAR(100) NOT NULL,
                        balance_paise BIGINT NOT NULL
                      )""");
            s.execute("INSERT INTO account VALUES (1, 'ann', 10000)");
            s.execute("INSERT INTO account VALUES (2, 'bob', 500)");
        } catch (SQLException e) {
            throw new IllegalStateException("could not create the schema", e);
        }
    }
}
