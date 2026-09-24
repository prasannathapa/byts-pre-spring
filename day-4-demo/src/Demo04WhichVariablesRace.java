import org.apache.catalina.startup.Tomcat;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 * DEMO 4: which variables are shared, and which are yours alone?
 *
 * Three threads call the SAME object. So:
 *   - an INSTANCE FIELD lives on that one object  -> everybody shares it  -> races
 *   - a LOCAL VARIABLE lives on the calling thread's own stack -> private -> safe
 *
 * Same servlet, both kinds, hammered by 200 requests at once. Watch which breaks.
 */
public class Demo04WhichVariablesRace {

    public static class Mixed extends HttpServlet {

        private String instanceField = "";      // ONE of these exists. Shared.

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

            String who = req.getParameter("who");   // a parameter: one per call, private
            String localVariable = who;             // a local: one per call, private

            instanceField = who;                    // everybody writing to the same slot

            // give another thread a chance to interleave between write and read
            Thread.yield();

            boolean localOk    = localVariable.equals(who);
            boolean instanceOk = instanceField.equals(who);

            res.getWriter().print(localOk + "," + instanceOk);
        }
    }

    public static void main(String[] args) throws Exception {
        quiet();
        Tomcat t = new Tomcat();
        t.setPort(0); t.getConnector();
        var ctx = t.addContext("", null);
        Tomcat.addServlet(ctx, "m", new Mixed());
        ctx.addServletMappingDecoded("/m", "m");
        t.start();
        int port = t.getConnector().getLocalPort();

        int n = 200;
        var pool = Executors.newFixedThreadPool(40);
        var client = HttpClient.newHttpClient();
        var results = new ConcurrentLinkedQueue<String>();
        var latch = new CountDownLatch(n);

        System.out.println("sending " + n + " requests, each with a different value...");
        for (int i = 0; i < n; i++) {
            final String value = "user" + i;
            pool.submit(() -> {
                try {
                    var r = client.send(HttpRequest.newBuilder(
                            URI.create("http://localhost:" + port + "/m?who=" + value)).build(),
                            HttpResponse.BodyHandlers.ofString());
                    results.add(r.body());
                } catch (Exception ignored) {
                } finally { latch.countDown(); }
            });
        }
        latch.await(60, TimeUnit.SECONDS);
        pool.shutdown();

        long localWrong = results.stream().filter(s -> s.startsWith("false")).count();
        long instanceWrong = results.stream().filter(s -> s.endsWith("false")).count();

        System.out.println();
        System.out.println("  local variable  read back wrong in " + localWrong + " of " + results.size() + " requests");
        System.out.println("  instance field  read back wrong in " + instanceWrong + " of " + results.size() + " requests");
        System.out.println();
        if (localWrong == 0 && instanceWrong > 0) {
            System.out.println("So: the local was ALWAYS right, the field was WRONG " + instanceWrong + " times.");
            System.out.println("Each thread got its own copy of the local. They all shared the one field.");
        } else if (instanceWrong == 0) {
            System.out.println("The field happened to survive this run. Run it again: that is what makes");
            System.out.println("this bug so nasty. It is not wrong every time, it is wrong unpredictably.");
        }
        System.out.println();
        System.out.println("Rule: anything you declare INSIDE the method is safe.");
        System.out.println("      anything you declare on the CLASS is shared by every request at once.");

        t.stop(); t.destroy();
    }

    static void quiet() {
        LogManager.getLogManager().reset();
        Logger r = Logger.getLogger(""); r.setLevel(Level.SEVERE);
        ConsoleHandler h = new ConsoleHandler(); h.setLevel(Level.SEVERE); r.addHandler(h);
    }
}
