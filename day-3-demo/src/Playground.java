import java.sql.*;

/**
 * PLAYGROUND: your scratch pad.
 *
 * The table is already made for you. Change the SQL, press run, see what happens.
 * You cannot break anything: the database is rebuilt from scratch every run.
 */
public class Playground {

    public static void main(String[] args) throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:play")) {
            setUp(conn);

            // ---- change the line below and run again -------------------------
            String sql = "select * from student order by marks desc";
            // ------------------------------------------------------------------

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                printTable(rs);
            }
        }
    }

    /** Prints any ResultSet without knowing its columns, using its metadata. */
    static void printTable(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int cols = md.getColumnCount();
        for (int i = 1; i <= cols; i++) System.out.printf("%-14s", md.getColumnLabel(i));
        System.out.println();
        for (int i = 1; i <= cols; i++) System.out.print("------------- ");
        System.out.println();
        while (rs.next()) {
            for (int i = 1; i <= cols; i++) System.out.printf("%-14s", rs.getString(i));
            System.out.println();
        }
    }

    static void setUp(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("create table student (id int primary key, name varchar(50), marks int, city varchar(30))");
            st.execute("""
                insert into student values
                  (1,'Ananya',78,'Chennai'), (2,'Ravi',64,'Pune'),
                  (3,'Meera',91,'Chennai'),  (4,'Karthik',55,'Kochi'),
                  (5,'Divya',83,'Pune')
                """);
        }
    }
}
