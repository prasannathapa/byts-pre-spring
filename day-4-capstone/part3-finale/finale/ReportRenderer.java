package finale;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Given. Stands in for the one dependency in every real application that costs real money to
 * create: a template engine, a PDF library, a client that opens a connection at construction.
 *
 * It counts its own constructions in BUILDS and sleeps for 200 ms while building, so
 * "we only made it when someone asked, and only once" is something a test can prove rather
 * than something you have to take on trust.
 */
public class ReportRenderer {

    public static final AtomicInteger BUILDS = new AtomicInteger();

    private final DataSource ds;

    public ReportRenderer(DataSource ds) {
        this.ds = ds;
        BUILDS.incrementAndGet();
        try {
            Thread.sleep(200);                       // pretend this is loading templates
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String render() {
        try (Connection c = ds.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*), SUM(balance_paise) FROM account")) {
            rs.next();
            return "report: " + rs.getLong(1) + " accounts holding " + rs.getLong(2) + " paise";
        } catch (SQLException e) {
            throw new IllegalStateException("the report could not be rendered", e);
        }
    }
}
