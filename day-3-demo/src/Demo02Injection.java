import java.sql.*;

/**
 * DEMO 2: why one of these two is a security hole.
 *
 * Both do "log this user in". One of them lets anybody in without a password.
 * Run it and watch the second login succeed with a password nobody knows.
 */
public class Demo02Injection {

    public static void main(String[] args) throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:login")) {
            setUp(conn);

            System.out.println("--- the honest attempt ---");
            System.out.println("  glued  : " + loginByGluing(conn, "ananya", "hunter2"));
            System.out.println("  bound  : " + loginByBinding(conn, "ananya", "hunter2"));

            System.out.println();
            System.out.println("--- the attack: the password is  ' OR '1'='1 ---");
            String attack = "' OR '1'='1";
            System.out.println("  glued  : " + loginByGluing(conn, "ananya", attack) + "   <-- let a stranger in");
            System.out.println("  bound  : " + loginByBinding(conn, "ananya", attack) + "   <-- refused, correctly");

            System.out.println();
            System.out.println("The glued version built this SQL:");
            System.out.println("  select count(*) from users where name = 'ananya' and password = '" + attack + "'");
            System.out.println("The database saw OR '1'='1, which is always true, so the password stopped mattering.");
            System.out.println("The bound version never built SQL out of the password at all. The ? stays a value.");
        }
    }

    /** NEVER do this. The user's text becomes part of the SQL. */
    static boolean loginByGluing(Connection conn, String user, String password) throws SQLException {
        String sql = "select count(*) from users where name = '" + user + "' and password = '" + password + "'";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    /** Do this. The SQL is fixed before the password is anywhere near it. */
    static boolean loginByBinding(Connection conn, String user, String password) throws SQLException {
        String sql = "select count(*) from users where name = ? and password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    static void setUp(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("create table users (name varchar(50), password varchar(50))");
            st.execute("insert into users values ('ananya', 'hunter2')");
        }
    }
}
