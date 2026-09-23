package finale;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import java.util.function.Supplier;

/**
 * THE COMPOSITION ROOT. This is the point of the whole day.
 *
 * One class builds the object graph, top to bottom, in one place, in one order you can read.
 * Nothing else in the application says `new` for a collaborator. Nothing is static. Nothing
 * goes looking for anything. When you can read the graph in twenty lines, you can change it.
 *
 * The constructor is private and start(int) is the only way in, so an App that exists is an
 * App that was wired properly. That is the useful half of the Singleton pattern: one object,
 * built one way, owning the lifecycle of everything under it.
 *
 * Wire it in this order:
 *
 *   1. CONFIGURATION lives here and only here. Build a JdbcDataSource (org.h2.jdbcx) on
 *      "jdbc:h2:mem:bank-" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1", with user "sa" and an
 *      empty password. A fresh name per App is what lets two Apps run side by side without
 *      quietly sharing a database, and one of the tests proves exactly that.
 *   2. Schema.create(dataSource).
 *   3. new JdbcAccountRepository(dataSource)      keep it in a field: repository() returns it.
 *   4. new AccountService(repository)
 *   5. the lazy one: Supplier<ReportRenderer> reports = memoise(() -> new ReportRenderer(ds));
 *      Building it costs 200 ms and most runs never ask for a report. The root decides WHAT
 *      to build without building it.
 *   6. new AccountServlet(service, reports), mapped at "/accounts/*".
 *   7. new LoggingFilter(log) on "/*", registered with FilterDef and FilterMap so the filter
 *      can be handed the log in its constructor. log() returns that RequestLog.
 *   8. Tomcat: temp baseDir, setPort(port), getConnector(), addContext("", null), mount, start.
 *
 * memoise() is four lines and it earns them. A plain `if (field == null) field = ...` under
 * twenty concurrent requests builds the renderer two or three times, and the test counts.
 * Double checked locking on a VOLATILE field is the fix: read the field once into a local,
 * and only if it is null take the lock and look again.
 */
public class App {

    // TODO: fields for the Tomcat instance, the repository and the RequestLog.

    private App(int port) {
        // TODO: steps 1 to 8 above. Private on purpose: start(port) is the only door.
        throw new UnsupportedOperationException("TODO: App(port)");
    }

    /** Builds the graph, then starts the server. */
    public static App start(int port) {
        // TODO: new App(port), start Tomcat, return it.
        throw new UnsupportedOperationException("TODO: App.start(port)");
    }

    public int port() {
        throw new UnsupportedOperationException("TODO: port()");
    }

    /** The very same repository the servlet is using. One graph, wired once. */
    public AccountRepository repository() {
        throw new UnsupportedOperationException("TODO: repository()");
    }

    public RequestLog log() {
        throw new UnsupportedOperationException("TODO: log()");
    }

    /** Stops Tomcat, destroys it, and drops the in-memory database. The root owns the ending too. */
    public void stop() {
        throw new UnsupportedOperationException("TODO: stop()");
    }

    /** Calls source once, on first get(), and hands back the same instance ever after. */
    static <T> Supplier<T> memoise(Supplier<T> source) {
        // TODO: double checked locking on a volatile field.
        throw new UnsupportedOperationException("TODO: memoise(source)");
    }
}
