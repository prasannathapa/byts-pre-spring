package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The lost update, live, and three ways to stop it.
 *
 * Run it:   ./demo/run.sh                (macOS / Linux)
 *           .\demo\run.cmd               (Windows)
 *           ./demo/run.sh atomic         and then lock, and then version
 *
 * Two tellers, one account, fifty cents each onto a balance of a hundred. The right
 * answer is two hundred every time. With no argument you get a hundred and fifty,
 * and you get it on every single run, because a latch holds both threads until both
 * of them have read the old number. There is no luck in here to blame.
 *
 * Nothing in the plain run is wrong Java. Every line is correct on its own. The bug
 * lives in the gap between the read and the write, which is exactly the gap the
 * three fixes close in three different ways.
 */
public class LostUpdate {

    static final String URL = "jdbc:h2:mem:lostupdate;DB_CLOSE_DELAY=-1";
    static final long START = 100, EACH = 50;

    static final AtomicInteger retries = new AtomicInteger();

    public static void main(String[] args) throws Exception {
        String mode = args.length == 0 ? "lose" : args[0].toLowerCase();
        if (!mode.equals("lose") && !mode.equals("atomic") && !mode.equals("lock") && !mode.equals("version")) {
            System.out.println("usage: run.sh [atomic | lock | version]   (no argument: watch the update get lost)");
            System.exit(2);
        }
        reset();

        // Both tellers wait here until both of them have read. In the fixed modes the
        // latch only lines them up at the start, which is as unfair as real traffic.
        CountDownLatch bothRead = new CountDownLatch(2);
        Thread a = teller(mode, bothRead), b = teller(mode, bothRead);
        a.start(); b.start(); a.join(); b.join();

        long got = balance();
        System.out.println("mode              : " + mode);
        System.out.println("start + 50 + 50   : " + (START + 2 * EACH) + "   (the only correct answer)");
        System.out.println("what the row says : " + got + (got == START + 2 * EACH ? "" : "   <-- one deposit was lost"));
        if (mode.equals("version")) System.out.println("retries           : " + retries.get());
    }

    static Thread teller(String mode, CountDownLatch bothRead) {
        return new Thread(() -> {
            try {
                switch (mode) {
                    case "atomic"  -> atomic(bothRead);
                    case "lock"    -> pessimistic(bothRead);
                    case "version" -> optimistic(bothRead);
                    default        -> teller(bothRead);
                }
            } catch (Exception e) {
                System.out.println("  a teller failed: " + e);
            }
        });
    }

    /** The losing run. This is what everybody writes the first time, and it reads perfectly well. */
    static void teller(CountDownLatch bothRead) throws Exception {
        try (Connection c = DriverManager.getConnection(URL)) {
            c.setAutoCommit(false);

            long b = readBalance(c, 1);        // both threads read 100
            bothRead.countDown();
            bothRead.await();                  // no luck involved: they both have 100

            try (PreparedStatement ps = c.prepareStatement(
                     "UPDATE account SET balance_cents = ? WHERE id = 1")) {
                ps.setLong(1, b + EACH);
                ps.executeUpdate();
            }
            c.commit();
        }
    }

    /** Fix 1. The read and the write are one statement, so there is no gap to lose anything in. */
    static void atomic(CountDownLatch bothRead) throws Exception {
        try (Connection c = DriverManager.getConnection(URL)) {
            c.setAutoCommit(false);
            bothRead.countDown();
            bothRead.await();
            try (PreparedStatement ps = c.prepareStatement(
                     "UPDATE account SET balance_cents = balance_cents + ? WHERE id = 1")) {
                ps.setLong(1, EACH);
                ps.executeUpdate();
            }
            c.commit();
        }
    }

    /** Fix 2. FOR UPDATE locks the row, so the second read waits for the first commit. */
    static void pessimistic(CountDownLatch bothRead) throws Exception {
        try (Connection c = DriverManager.getConnection(URL)) {
            c.setAutoCommit(false);
            bothRead.countDown();
            bothRead.await();

            long b;
            try (PreparedStatement q = c.prepareStatement(
                     "SELECT balance_cents FROM account WHERE id = 1 FOR UPDATE");
                 ResultSet rs = q.executeQuery()) {
                rs.next();
                b = rs.getLong(1);             // the second teller is asleep on this line
            }
            try (PreparedStatement ps = c.prepareStatement(
                     "UPDATE account SET balance_cents = ? WHERE id = 1")) {
                ps.setLong(1, b + EACH);
                ps.executeUpdate();
            }
            c.commit();
        }
    }

    /** Fix 3. Write only if nobody moved. If nobody matched, somebody beat you: read again and retry. */
    static void optimistic(CountDownLatch bothRead) throws Exception {
        try (Connection c = DriverManager.getConnection(URL)) {
            c.setAutoCommit(false);
            boolean first = true;
            while (true) {
                long b, version;
                try (PreparedStatement q = c.prepareStatement(
                         "SELECT balance_cents, version FROM account WHERE id = 1");
                     ResultSet rs = q.executeQuery()) {
                    rs.next();
                    b = rs.getLong(1);
                    version = rs.getLong(2);
                }
                if (first) {                   // both tellers read version 0, on purpose
                    bothRead.countDown();
                    bothRead.await();
                    first = false;
                }
                int changed;
                try (PreparedStatement ps = c.prepareStatement(
                         "UPDATE account SET balance_cents = ?, version = version + 1 "
                       + "WHERE id = 1 AND version = ?")) {
                    ps.setLong(1, b + EACH);
                    ps.setLong(2, version);
                    changed = ps.executeUpdate();
                }
                c.commit();
                if (changed == 1) return;
                retries.incrementAndGet();     // the loser pays one extra round trip, and that is all
            }
        }
    }

    static long readBalance(Connection c, long id) throws SQLException {
        try (PreparedStatement q = c.prepareStatement("SELECT balance_cents FROM account WHERE id = ?")) {
            q.setLong(1, id);
            try (ResultSet rs = q.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    static void reset() throws SQLException {
        try (Connection c = DriverManager.getConnection(URL);
             Statement s = c.createStatement()) {
            s.execute("DROP TABLE IF EXISTS account");
            s.execute("CREATE TABLE account (id BIGINT PRIMARY KEY, balance_cents BIGINT NOT NULL, version BIGINT NOT NULL)");
            s.execute("INSERT INTO account VALUES (1, " + START + ", 0)");
        }
    }

    static long balance() throws SQLException {
        try (Connection c = DriverManager.getConnection(URL)) {
            return readBalance(c, 1);
        }
    }
}
