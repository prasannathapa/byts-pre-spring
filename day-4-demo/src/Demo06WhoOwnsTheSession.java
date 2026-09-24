import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.core.StandardContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

/**
 * DEMO 6: who does a session belong to, and what does a listener listen to?
 *
 * Two questions people get wrong:
 *   1. Is a session per servlet?   No. It belongs to the CLIENT (the browser).
 *   2. Is a listener per servlet?  No. It is registered for the whole WEBAPP.
 *
 * We run two different servlets and two different "browsers" and watch.
 */
public class Demo06WhoOwnsTheSession {

    /** ONE listener. Not attached to any servlet. It hears every session in the app. */
    public static class Counter implements HttpSessionListener {
        static final AtomicInteger live = new AtomicInteger();

        @Override public void sessionCreated(HttpSessionEvent e) {
            System.out.println("   [listener] a session was CREATED: " + e.getSession().getId().substring(0, 8)
                               + "   live now: " + live.incrementAndGet());
        }
        @Override public void sessionDestroyed(HttpSessionEvent e) {
            System.out.println("   [listener] a session was DESTROYED: " + e.getSession().getId().substring(0, 8)
                               + " live now: " + live.decrementAndGet());
        }
    }

    /** Servlet A: puts something in the session. */
    public static class Login extends HttpServlet {
        @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            HttpSession s = req.getSession();           // creates one if there is none
            s.setAttribute("user", req.getParameter("who"));
            res.getWriter().print("Login  used session " + s.getId().substring(0, 8));
        }
    }

    /** Servlet B: a DIFFERENT class, reading what A put there. */
    public static class Profile extends HttpServlet {
        @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            HttpSession s = req.getSession();
            res.getWriter().print("Profile used session " + s.getId().substring(0, 8)
                                  + " and found user = " + s.getAttribute("user"));
        }
    }

    public static void main(String[] args) throws Exception {
        quiet();
        Tomcat t = new Tomcat();
        t.setPort(0); t.getConnector();
        var ctx = (StandardContext) t.addContext("", null);
        // ONE listener, registered on the webapp. This is what <listener> in web.xml does.
        ctx.addApplicationLifecycleListener(new Counter());
        Tomcat.addServlet(ctx, "login", new Login());   ctx.addServletMappingDecoded("/login", "login");
        Tomcat.addServlet(ctx, "profile", new Profile()); ctx.addServletMappingDecoded("/profile", "profile");
        t.start();
        int port = t.getConnector().getLocalPort();
        String base = "http://localhost:" + port;

        System.out.println("=== browser 1 visits /login, then /profile ===");
        var browser1 = client();
        System.out.println("  " + get(browser1, base + "/login?who=ananya"));
        System.out.println("  " + get(browser1, base + "/profile"));
        System.out.println("  ^ SAME session id, and Profile read what Login wrote.");
        System.out.println("    The session follows the BROWSER, not the servlet.");

        System.out.println();
        System.out.println("=== browser 2, a different cookie jar, visits /profile ===");
        var browser2 = client();
        System.out.println("  " + get(browser2, base + "/profile"));
        System.out.println("  ^ a NEW session id, and user is null. Different visitor, different session.");

        System.out.println();
        System.out.println("The listener printed a line for BOTH sessions, even though one was");
        System.out.println("created by Login and the other by Profile. It is not tied to either.");
        System.out.println("Registered once for the webapp, it hears every session in the app.");

        t.stop(); t.destroy();
    }

    /** A client with its own cookie jar, which is what makes it a separate "browser". */
    static HttpClient client() {
        return HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
    }
    static String get(HttpClient c, String url) throws Exception {
        return c.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString()).body();
    }
    static void quiet() {
        LogManager.getLogManager().reset();
        Logger r = Logger.getLogger(""); r.setLevel(Level.SEVERE);
        ConsoleHandler h = new ConsoleHandler(); h.setLevel(Level.SEVERE); r.addHandler(h);
    }
}
