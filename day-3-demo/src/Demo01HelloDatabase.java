import java.sql.*;

/**
 * DEMO 1: the whole of JDBC on one screen.
 *
 * Nothing is installed. No server is running. The database lives inside this
 * program's memory and disappears when the program ends. That is what
 * "jdbc:h2:mem:" means: h2 is the database, mem is where it lives.
 *
 * Read the five steps below. Every JDBC program you will ever write is these
 * five steps, and everything else in the day is detail hung off them.
 */
public class Demo01HelloDatabase {

    public static void main(String[] args) throws Exception {

        // STEP 1. Open a connection. Think of it as dialling the database and
        // keeping the line open. try-with-resources hangs up for you at the end,
        // even if something throws.
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:demo")) {

            System.out.println("connected to: " + conn.getMetaData().getDatabaseProductName());
            System.out.println();

            // STEP 2. Send a statement that changes the database. There is no
            // result to read back, so we use execute/executeUpdate, not a query.
            try (Statement st = conn.createStatement()) {
                st.execute("""
                    create table student (
                        id    int primary key,
                        name  varchar(50),
                        marks int
                    )
                    """);
                System.out.println("created a table called student");
            }

            // STEP 3. Put some rows in. The ? are blanks we fill in afterwards,
            // one setX call per blank, counting from 1 (not 0).
            String insert = "insert into student (id, name, marks) values (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                Object[][] rows = {
                    {1, "Ananya", 78},
                    {2, "Ravi",   64},
                    {3, "Meera",  91},
                };
                for (Object[] row : rows) {
                    ps.setInt(1, (Integer) row[0]);
                    ps.setString(2, (String) row[1]);
                    ps.setInt(3, (Integer) row[2]);
                    ps.executeUpdate();           // returns how many rows changed
                }
                System.out.println("inserted " + rows.length + " students");
            }
            System.out.println();

            // STEP 4. Ask a question and read the answer. A ResultSet is a cursor
            // sitting just BEFORE the first row; next() moves it on and returns
            // false when there is nothing left. That is why it is a while loop.
            String query = "select name, marks from student order by marks desc";
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(query)) {

                System.out.println("students, best first:");
                while (rs.next()) {
                    String name  = rs.getString("name");
                    int    marks = rs.getInt("marks");
                    System.out.printf("   %-8s %3d%n", name, marks);
                }
            }

            // STEP 5. Close everything. try-with-resources already did it, in the
            // reverse order it opened them. That is the whole reason to use it.
        }

        System.out.println();
        System.out.println("connection closed, and the in-memory database is gone with it.");
    }
}
