package transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Searching by a string the user typed. Two different kinds of danger live here.
 *
 * The first is SQL injection, and a ? fixes it: a bound value is never parsed as SQL,
 * so "x' OR '1'='1" is just an odd name that matches nobody.
 *
 * The second one survives the fix. In a LIKE pattern, % means "anything" and _ means
 * "any one character", and those are still special AFTER binding, because they are
 * special to LIKE rather than to the parser. A customer who types % into your search
 * box would get every row in the table. So you escape the user's text yourself and
 * tell LIKE which character you escaped with: LIKE ? ESCAPE '!'.
 */
public final class AccountSearch {

    private static final String BY_OWNER  = "SELECT id, owner, balance_cents FROM account WHERE owner = ? ORDER BY id";
    private static final String BY_PREFIX = "SELECT id, owner, balance_cents FROM account WHERE owner LIKE ? ESCAPE '!' ORDER BY id";

    private AccountSearch() { }

    /** Every account with exactly this owner name. */
    public static List<Account> findByOwner(Connection c, String owner) throws SQLException {
        // TODO: prepare BY_OWNER, bind the name, collect the rows through toAccount.
        //       Do not build this SQL with +. The injection test is not subtle about it.
        throw new UnsupportedOperationException("TODO: AccountSearch.findByOwner(connection, owner)");
    }

    /** Every account whose owner name starts with this text, taken literally. */
    public static List<Account> findByOwnerPrefix(Connection c, String prefix) throws SQLException {
        // TODO: bind esc(prefix) + "%" into BY_PREFIX and collect the rows.
        //       The trailing % is OURS and stays a wildcard. Anything the user typed
        //       has already been defused by esc.
        throw new UnsupportedOperationException("TODO: AccountSearch.findByOwnerPrefix(connection, prefix)");
    }

    /** Makes %, _ and the escape character itself into ordinary text. */
    private static String esc(String s) {
        // TODO: replace "!" with "!!" FIRST (or you will escape your own escapes),
        //       then "%" with "!%", then "_" with "!_".
        throw new UnsupportedOperationException("TODO: AccountSearch.esc(text)");
    }

    private static Account toAccount(ResultSet rs) throws SQLException {
        return new Account(rs.getLong("id"), rs.getString("owner"), rs.getLong("balance_cents"));
    }

    /** Runs a one-parameter query and maps every row. Use it or write your own. */
    private static List<Account> query(Connection c, String sql, String arg) throws SQLException {
        List<Account> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, arg);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(toAccount(rs));
            }
        }
        return out;
    }
}
