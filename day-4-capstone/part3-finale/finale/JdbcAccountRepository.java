package finale;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Given, and it is Day 3 almost word for word: PreparedStatement everywhere,
 * try-with-resources everywhere, and one real transaction.
 *
 * Read transfer() slowly. Autocommit goes off, both rows are read INSIDE the transaction,
 * the rule is checked there, and any failure rolls the whole thing back in the catch. The
 * balance check cannot live in the servlet: between a read there and a write here, another
 * request can spend the money.
 */
public class JdbcAccountRepository implements AccountRepository {

    private final DataSource ds;

    public JdbcAccountRepository(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Optional<Account> findById(long id) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, owner, balance_paise FROM account WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("findById(" + id + ") failed", e);
        }
    }

    @Override
    public List<Account> findAll() {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, owner, balance_paise FROM account ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            List<Account> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return List.copyOf(out);
        } catch (SQLException e) {
            throw new IllegalStateException("findAll failed", e);
        }
    }

    @Override
    public Account save(Account account) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "MERGE INTO account (id, owner, balance_paise) KEY (id) VALUES (?, ?, ?)")) {
            ps.setLong(1, account.id());
            ps.setString(2, account.owner());
            ps.setLong(3, account.balancePaise());
            ps.executeUpdate();
            return account;
        } catch (SQLException e) {
            throw new IllegalStateException("save(" + account.id() + ") failed", e);
        }
    }

    @Override
    public void transfer(long fromId, long toId, long amountPaise) {
        Connection c = null;
        try {
            c = ds.getConnection();
            c.setAutoCommit(false);
            try {
                long fromBalance = balanceFor(c, fromId);
                balanceFor(c, toId);                       // proves the destination exists, inside the transaction
                if (fromBalance < amountPaise) {
                    throw new InsufficientFundsException("account " + fromId + " holds " + fromBalance
                            + " paise and cannot send " + amountPaise);
                }
                move(c, fromId, -amountPaise);
                move(c, toId, amountPaise);
                c.commit();
            } catch (RuntimeException | SQLException failure) {
                c.rollback();
                throw failure;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("transfer failed", e);
        } finally {
            close(c);
        }
    }

    private static long balanceFor(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT balance_paise FROM account WHERE id = ? FOR UPDATE")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new java.util.NoSuchElementException("no account " + id);
                return rs.getLong(1);
            }
        }
    }

    private static void move(Connection c, long id, long delta) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE account SET balance_paise = balance_paise + ? WHERE id = ?")) {
            ps.setLong(1, delta);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    private static Account map(ResultSet rs) throws SQLException {
        return new Account(rs.getLong("id"), rs.getString("owner"), rs.getLong("balance_paise"));
    }

    private static void close(Connection c) {
        if (c == null) return;
        try {
            c.close();
        } catch (SQLException ignored) {
            // nothing useful to do while closing
        }
    }
}
