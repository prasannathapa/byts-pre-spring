import org.apache.catalina.startup.Tomcat;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.logging.*;

/**
 * DEMO 2: who creates your servlet, and when?
 *
 * You never write "new CountingServlet()". The container does it, once, and
 * then calls the same object for every request that arrives. Watch the order
 * the messages print in, and watch how many times each one prints.
 */
public class Demo02Lifecycle {

    public static class Watched extends HttpServlet {

        public Watched() {
            System.out.println("   [1] constructor   <- the container made the object");
        }

        @Override public void init() {
            System.out.println("   [2] init()        <- once, before any request. Set up expensive things here.");
        }

        @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            System.out.println("   [3] doGet()       <- once PER REQUEST, on thread " + Thread.currentThread().getName());
            res.getWriter().print("ok");
        }

        @Override public void destroy() {
            System.out.println("   [4] destroy()     <- once, on shutdown. Release things here.");
        }
    }

    public static void main(String[] args) throws Exception {
        quiet();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(0); tomcat.getConnector();
        var ctx = tomcat.addContext("", null);
        Tomcat.addServlet(ctx, "w", new Watched());
        ctx.addServletMappingDecoded("/w", "w");

        System.out.println("starting the server...");
        tomcat.start();
        int port = tomcat.getConnector().getLocalPort();
        System.out.println();

        System.out.println("sending three requests:");
        var client = HttpClient.newHttpClient();
        for (int i = 1; i <= 3; i++) {
            client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/w")).build(),
                        HttpResponse.BodyHandlers.ofString());
        }
        System.out.println();

        System.out.println("stopping the server...");
        tomcat.stop(); tomcat.destroy();
        System.out.println();
        System.out.println("Constructor once. init() once. doGet() three times. destroy() once.");
        System.out.println("ONE object served all three requests. Remember that for the next demo.");
    }

    static void quiet() {
        LogManager.getLogManager().reset();
        Logger r = Logger.getLogger(""); r.setLevel(Level.WARNING);
        ConsoleHandler h = new ConsoleHandler(); h.setLevel(Level.WARNING); r.addHandler(h);
    }
}
