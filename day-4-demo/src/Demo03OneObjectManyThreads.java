import org.apache.catalina.startup.Tomcat;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

/**
 * DEMO 3: the bug that only shows up with real traffic.
 *
 * There is ONE servlet object. Every request runs through it, on a different
 * thread, at the same time. So an ordinary field on a servlet is shared by
 * everybody at once, and "count = count + 1" is not one step.
 *
 * We fire 500 requests at two counters and compare. Both should end at 500.
 */
public class Demo03OneObjectManyThreads {

    /** BROKEN. An ordinary int field, touched by many threads at once. */
    public static class Unsafe extends HttpServlet {
        private int count = 0;
        @Override protected void doGet(HttpServletRequest q, HttpServletResponse s) throws IOException {
            int seen = count;          // read
            Thread.yield();            // the gap where another thread gets in
            count = seen + 1;          // write back what we read, which may now be stale
            s.getWriter().print(count);
        }
        int value() { return count; }
    }

    /** FIXED. One indivisible add, so no thread can slip into the gap. */
    public static class Safe extends HttpServlet {
        private final AtomicInteger count = new AtomicInteger();
        @Override protected void doGet(HttpServletRequest q, HttpServletResponse s) throws IOException {
            s.getWriter().print(count.incrementAndGet());
        }
        int value() { return count.get(); }
    }

    public static void main(String[] args) throws Exception {
        quiet();
        Unsafe unsafe = new Unsafe();
        Safe safe = new Safe();

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(0); tomcat.getConnector();
        var ctx = tomcat.addContext("", null);
        Tomcat.addServlet(ctx, "u", unsafe); ctx.addServletMappingDecoded("/unsafe", "u");
        Tomcat.addServlet(ctx, "s", safe);   ctx.addServletMappingDecoded("/safe", "s");
        tomcat.start();
        int port = tomcat.getConnector().getLocalPort();

        int hits = 500;
        System.out.println("firing " + hits + " requests at each counter, 50 at a time...");
        System.out.println();
        hammer(port, "/unsafe", hits);
        hammer(port, "/safe", hits);

        System.out.println("  plain int field   : expected " + hits + ", got " + unsafe.value()
                           + (unsafe.value() == hits ? "   (got lucky this run, try again)" : "   <- LOST " + (hits - unsafe.value()) + " COUNTS"));
        System.out.println("  AtomicInteger     : expected " + hits + ", got " + safe.value()
                           + (safe.value() == hits ? "   correct, every time" : "   <- unexpected"));
        System.out.println();
        System.out.println("The broken one is not broken in your IDE, with one user, clicking slowly.");
        System.out.println("It is broken in production, and the number is quietly wrong forever.");

        tomcat.stop(); tomcat.destroy();
    }

    static void hammer(int port, String path, int hits) throws Exception {
        var pool = Executors.newFixedThreadPool(50);
        var client = HttpClient.newHttpClient();
        var latch = new CountDownLatch(hits);
        for (int i = 0; i < hits; i++) {
            pool.submit(() -> {
                try {
                    client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).build(),
                                HttpResponse.BodyHandlers.discarding());
                } catch (Exception ignored) {
                } finally { latch.countDown(); }
            });
        }
        latch.await(60, TimeUnit.SECONDS);
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
    }

    static void quiet() {
        LogManager.getLogManager().reset();
        Logger r = Logger.getLogger(""); r.setLevel(Level.SEVERE);
        ConsoleHandler h = new ConsoleHandler(); h.setLevel(Level.SEVERE); r.addHandler(h);
    }
}
