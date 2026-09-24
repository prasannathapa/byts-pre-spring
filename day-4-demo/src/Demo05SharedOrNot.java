import org.apache.catalina.startup.Tomcat;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 * DEMO 5: is the application scope one map, or one map each?
 *
 * Two DIFFERENT servlet classes both read the same attribute. We print the
 * object's identity so you can see whether they are looking at one object or
 * at copies. A ThreadLocal is shown next to it, because that IS one each.
 */
public class Demo05SharedOrNot {

    /** Servlet A. Reads the shared map, writes one entry into it. */
    public static class Alpha extends HttpServlet {
        static final ThreadLocal<String> perThread = ThreadLocal.withInitial(() -> "unset");

        @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            @SuppressWarnings("unchecked")
            Map<String, Long> prices = (Map<String, Long>) getServletContext().getAttribute("prices");
            prices.put("from-Alpha", 1L);
            perThread.set("set by Alpha on " + Thread.currentThread().getName());

            res.getWriter().print(
                "Alpha sees map id " + System.identityHashCode(prices)
                + ", size " + prices.size()
                + " | its ThreadLocal says: " + perThread.get());
        }
    }

    /** Servlet B. A completely different class, in a different mapping. */
    public static class Beta extends HttpServlet {
        @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            @SuppressWarnings("unchecked")
            Map<String, Long> prices = (Map<String, Long>) getServletContext().getAttribute("prices");
            prices.put("from-Beta", 2L);

            res.getWriter().print(
                "Beta  sees map id " + System.identityHashCode(prices)
                + ", size " + prices.size()
                + ", keys " + new TreeSet<>(prices.keySet())
                + " | Alpha's ThreadLocal says: " + Alpha.perThread.get());
        }
    }

    public static void main(String[] args) throws Exception {
        quiet();
        Tomcat t = new Tomcat();
        t.setPort(0); t.getConnector();
        var ctx = t.addContext("", null);
        Tomcat.addServlet(ctx, "a", new Alpha()); ctx.addServletMappingDecoded("/a", "a");
        Tomcat.addServlet(ctx, "b", new Beta());  ctx.addServletMappingDecoded("/b", "b");
        t.start();
        int port = t.getConnector().getLocalPort();

        // put ONE map into the application scope, exactly as the slide does
        Map<String, Long> prices = new ConcurrentHashMap<>();
        ctx.getServletContext().setAttribute("prices", prices);
        System.out.println("main() put ONE map into the application scope. Its id is "
                           + System.identityHashCode(prices));
        System.out.println();

        var client = HttpClient.newHttpClient();
        for (String path : List.of("/a", "/b", "/a", "/b")) {
            var r = client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).build(),
                                HttpResponse.BodyHandlers.ofString());
            System.out.println("  " + r.body());
        }

        System.out.println();
        System.out.println("The map id is the SAME everywhere, including in main(). One object.");
        System.out.println("Both servlets wrote into it, and each saw the other's key. It is shared.");
        System.out.println();
        System.out.println("The ThreadLocal is the opposite: Beta asked Alpha's ThreadLocal and got");
        System.out.println("'unset', because a ThreadLocal holds one value PER THREAD, not per app.");
        System.out.println();
        System.out.println("That is why the slide says ConcurrentHashMap, thread safe on purpose:");
        System.out.println("one object, reachable by every servlet on every request thread at once.");

        t.stop(); t.destroy();
    }

    static void quiet() {
        LogManager.getLogManager().reset();
        Logger r = Logger.getLogger(""); r.setLevel(Level.SEVERE);
        ConsoleHandler h = new ConsoleHandler(); h.setLevel(Level.SEVERE); r.addHandler(h);
    }
}
