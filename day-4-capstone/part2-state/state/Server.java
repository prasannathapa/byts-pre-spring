package state;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

/**
 * Given to you, because you did this by hand in part 1. Read it anyway: it is the whole
 * wiring of the application in one constructor, and that is where part 3 is going.
 *
 * Three things are worth your attention.
 *
 * The filter is registered with FilterDef plus FilterMap rather than @WebFilter, so it can be
 * handed the RequestLog in its constructor. Annotation scanning would make the container call
 * a no-argument constructor, and then the filter would have to go and find its collaborators
 * itself. Constructor injection beats a lookup every time.
 *
 * The listener is registered by class name before start(), because contextInitialized fires
 * during start and a listener added after that has already missed its own event. The container
 * builds it with its no-argument constructor, which is exactly the limitation that makes
 * FilterDef worth using for the filter.
 *
 * NotFoundServlet is mapped at the exact pattern "/". That pattern means "the default servlet":
 * anything no other mapping claimed. Without it, an unknown URL is answered by the container
 * before any filter chain is built, so your filter would never see it and the miss would never
 * be logged. One line of mapping turns a container 404 into a request your code owns.
 */
public class Server {

    private final Tomcat tomcat = new Tomcat();
    private final RequestLog log = new RequestLog();

    public Server(int port) {
        try {
            tomcat.setBaseDir(Files.createTempDirectory("tc").toString());
        } catch (IOException e) {
            throw new UncheckedIOException("could not make a scratch directory for Tomcat", e);
        }
        tomcat.setPort(port);
        tomcat.getConnector();
        Context ctx = tomcat.addContext("", null);

        ctx.addApplicationListener(StartupListener.class.getName());

        FilterDef def = new FilterDef();
        def.setFilterName("requestLog");
        def.setFilter(new RequestLogFilter(log));
        ctx.addFilterDef(def);
        FilterMap map = new FilterMap();
        map.setFilterName("requestLog");
        map.addURLPattern("/*");
        ctx.addFilterMap(map);

        mount(ctx, "visit", new VisitServlet(), "/visit");
        mount(ctx, "hits", new HitServlet(), "/hits");
        mount(ctx, "info", new InfoServlet(), "/info");
        mount(ctx, "new", new NewServlet(), "/new");
        mount(ctx, "old", new OldServlet(), "/old");
        mount(ctx, "alias", new AliasServlet(), "/alias");
        mount(ctx, "admin", new AdminServlet(), "/admin/*");
        mount(ctx, "notFound", new NotFoundServlet(), "/");
    }

    private static void mount(Context ctx, String name, HttpServlet servlet, String pattern) {
        Tomcat.addServlet(ctx, name, servlet);
        ctx.addServletMapping(pattern, name);
    }

    public void start() {
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new IllegalStateException("Tomcat refused to start", e);
        }
    }

    public int port() {
        return tomcat.getConnector().getLocalPort();
    }

    public RequestLog log() {
        return log;
    }

    public void stop() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new IllegalStateException("Tomcat refused to stop", e);
        }
    }
}

/** Given. Anything under /admin that got past the filter. Reaching this body means the guard let you through. */
class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain;charset=UTF-8");
        res.getWriter().print("admin ok " + req.getPathInfo());
    }
}

/** Given. The catch-all at "/", so a miss is still a request your filter sees. */
class NotFoundServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain;charset=UTF-8");
        res.setStatus(404);
        res.getWriter().print("no such path: " + req.getRequestURI());
    }
}
