import java.sql.*;
import java.util.*;

/**
 * DEMO 4: stop passing ResultSet around.
 *
 * A ResultSet is only valid while its statement and connection are open. Turn
 * each row into an ordinary Java object at the edge, and the rest of your
 * program never has to know a database exists.
 */
public class Demo04RowsToObjects {

    /** An ordinary object. Nothing in here knows about SQL. */
    record Student(int id, String name, int marks) {}

    public static void main(String[] args) throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:rows")) {
            setUp(conn);

            List<Student> all = findAll(conn);
            System.out.println("as objects:");
            all.forEach(s -> System.out.println("   " + s));

            System.out.println();
            System.out.println("and now ordinary Java works on them, no database in sight:");
            all.stream()
               .filter(s -> s.marks() >= 70)
               .map(Student::name)
               .forEach(n -> System.out.println("   passed well: " + n));

            double avg = all.stream().mapToInt(Student::marks).average().orElse(0);
            System.out.printf("   average: %.1f%n", avg);
        }
    }

    static List<Student> findAll(Connection conn) throws SQLException {
        List<Student> out = new ArrayList<>();
        String sql = "select id, name, marks from student order by id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Student(rs.getInt("id"), rs.getString("name"), rs.getInt("marks")));
            }
        }
        // the ResultSet is closed here, and it does not matter: we already have objects
        return out;
    }

    static void setUp(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("create table student (id int primary key, name varchar(50), marks int)");
            st.execute("insert into student values (1,'Ananya',78), (2,'Ravi',64), (3,'Meera',91)");
        }
    }
}
