import org.apache.catalina.startup.Tomcat;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.logging.*;

/**
 * DEMO 1: what a web server actually is.
 *
 * Before any servlet talk, here is the whole idea in one program:
 * a web server is an ordinary Java program that
 *   1. opens a port and waits,
 *   2. reads the text a browser sends,
 *   3. writes text back.
 * That is all. Tomcat is that program, written properly, by other people.
 *
 * Nothing is installed here. Tomcat is just a jar on the classpath, and we
 * start it from main() like any other object.
 */
public class Demo01WhatIsAServer {

    /** Your code. The container calls doGet when a GET arrives for your URL. */
    public static class Hello extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            System.out.println("   [server] a request arrived for " + req.getRequestURI()
                               + ", handled on thread " + Thread.currentThread().getName());
            res.setStatus(200);
            res.setContentType("text/plain");
            res.getWriter().print("hello from a servlet");
        }
    }

    public static void main(String[] args) throws Exception {
        quiet();

        System.out.println("1. building a Tomcat object. Nothing is listening yet.");
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(0);                 // 0 means "pick any free port for me"
        tomcat.getConnector();             // the connector is the thing that owns the socket

        System.out.println("2. registering our class against the path /hello");
        var ctx = tomcat.addContext("", null);
        Tomcat.addServlet(ctx, "hello", new Hello());
        ctx.addServletMappingDecoded("/hello", "hello");
        System.out.println("   note: we never called new Hello() ourselves later. We handed the object over.");

        System.out.println("3. start(). NOW a port is open and a thread pool is waiting.");
        tomcat.start();
        int port = tomcat.getConnector().getLocalPort();
        System.out.println("   listening on http://localhost:" + port);
        System.out.println();

        System.out.println("4. pretending to be a browser and asking for /hello");
        var response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/hello")).build(),
                HttpResponse.BodyHandlers.ofString());
        System.out.println("   [client] status " + response.statusCode());
        System.out.println("   [client] body   " + response.body());
        System.out.println();

        System.out.println("5. stop(). The port closes and the threads go away.");
        tomcat.stop();
        tomcat.destroy();
        System.out.println();
        System.out.println("That is a web server: a program holding a port open, reading text and writing text back.");
    }

    /** Tomcat is chatty. Turn it down so you can read our own output. */
    static void quiet() {
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger("");
        root.setLevel(Level.WARNING);
        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.WARNING);
        root.addHandler(h);
    }
}
