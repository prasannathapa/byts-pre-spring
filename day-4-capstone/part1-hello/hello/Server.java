package hello;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

/**
 * Your embedded container. No WAR, no installed Tomcat, no web.xml: a main method
 * that builds a server, maps two servlets and starts listening.
 *
 * Port 0 means "any free port the OS will give me". That is why the tests can run on
 * any machine without asking whether 8080 is busy, and it is why port() has to ask the
 * connector what it actually got rather than repeating back the 0 you passed in.
 *
 * The pieces you need, in order:
 *   Tomcat t = new Tomcat();
 *   t.setBaseDir(...)                        a scratch directory; use a temp one, see below
 *   t.setPort(port);
 *   t.getConnector();                        this call is what creates the connector
 *   Context ctx = t.addContext("", null);    the root context, "" not "/"
 *   Tomcat.addServlet(ctx, "name", servletInstance);
 *   ctx.addServletMapping("/path", "name");
 *   t.start();  t.getConnector().getLocalPort();  t.stop();  t.destroy();
 */
public class Server {

    // TODO: hold the Tomcat instance in a field so start(), port() and stop() can all reach it.

    public Server(int port) {
        // TODO: build Tomcat, point setBaseDir at Files.createTempDirectory("tc").toString()
        //       so no tomcat.0 folder appears next to your code, set the port, ask for the
        //       connector, add the root context, then register:
        //         HelloServlet  on  /hello
        //         SquareServlet on  /square
        throw new UnsupportedOperationException("TODO: Server(port)");
    }

    public void start() {
        // TODO: start Tomcat. LifecycleException is checked: wrap it in an IllegalStateException.
        throw new UnsupportedOperationException("TODO: start()");
    }

    public int port() {
        // TODO: the port the connector is really listening on, not the one you were given.
        throw new UnsupportedOperationException("TODO: port()");
    }

    public void stop() {
        // TODO: stop AND destroy. Stop alone leaves the socket held, and the last test
        //       checks that the port is genuinely gone.
        throw new UnsupportedOperationException("TODO: stop()");
    }
}
