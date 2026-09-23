package demo;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.nio.file.Files;

/**
 * The whole of a web application, minus the web application. Run it, open
 * http://localhost:8080/hello?name=you, and stop it with Ctrl-C.
 *
 * No WAR file, no installed server, no web.xml, no Spring. A main method that builds a
 * container, hands it one servlet and starts listening. Everything else today is detail
 * on top of these nine lines.
 */
public class App {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(Files.createTempDirectory("tc").toString());
        tomcat.setPort(8080);
        tomcat.getConnector();
        Context ctx = tomcat.addContext("", null);
        Tomcat.addServlet(ctx, "hello", new HelloServlet());
        ctx.addServletMappingDecoded("/hello", "hello");
        tomcat.start();
        System.out.println("listening on http://localhost:" + tomcat.getConnector().getLocalPort() + "/hello?name=you");
        tomcat.getServer().await();
    }
}
