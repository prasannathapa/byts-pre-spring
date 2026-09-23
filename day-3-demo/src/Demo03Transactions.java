import java.sql.*;

/**
 * DEMO 3: money must not vanish.
 *
 * A transfer is two updates. If the second one fails after the first one
 * succeeded, money has been destroyed. A transaction is how you say
 * "both, or neither".
 */
public class Demo03Transactions {

    public static void main(String[] args) throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:bank")) {
            setUp(conn);

            System.out.println("start:        " + balances(conn));

            System.out.println();
            System.out.println("--- transfer 300 from A to B, and it works ---");
            transfer(conn, "A", "B", 300);
            System.out.println("after:        " + balances(conn));

            System.out.println();
            System.out.println("--- now transfer 999999, which A cannot afford ---");
            try {
                transfer(conn, "A", "B", 999999);
            } catch (Exception e) {
                System.out.println("  refused: " + e.getMessage());
            }
            System.out.println("after:        " + balances(conn) + "   <-- unchanged, because we rolled back");
        }
    }

    static void transfer(Connection conn, String from, String to, int amount) throws SQLException {
        // By default every statement commits the moment it runs. Turn that off,
        // and nothing is permanent until YOU say commit.
        conn.setAutoCommit(false);
        try {
            take(conn, from, amount);   // if this succeeds
            give(conn, to, amount);     // ...and this throws, we must undo the first
            conn.commit();              // both worked: make it permanent
        } catch (SQLException e) {
            conn.rollback();            // undo everything since setAutoCommit(false)
            throw e;
        } finally {
            conn.setAutoCommit(true);   // put the connection back as we found it
        }
    }

    static void take(Connection conn, String who, int amount) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "update account set balance = balance - ? where id = ? and balance >= ?")) {
            ps.setInt(1, amount); ps.setString(2, who); ps.setInt(3, amount);
            if (ps.executeUpdate() == 0) throw new SQLException(who + " does not have " + amount);
        }
    }

    static void give(Connection conn, String who, int amount) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "update account set balance = balance + ? where id = ?")) {
            ps.setInt(1, amount); ps.setString(2, who);
            ps.executeUpdate();
        }
    }

    static String balances(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("select id, balance from account order by id")) {
            while (rs.next()) sb.append(rs.getString("id")).append("=").append(rs.getInt("balance")).append("  ");
        }
        return sb.toString().trim();
    }

    static void setUp(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("create table account (id varchar(4) primary key, balance int)");
            st.execute("insert into account values ('A', 1000), ('B', 500)");
        }
    }
}
