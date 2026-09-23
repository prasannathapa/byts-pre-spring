package state;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;

/**
 * Given to you. It reads application scope and prints it: "byts-bank 2026-09-22T09:15:00Z".
 *
 * It does not create either value. Something has to put them there before the first request
 * arrives, and a servlet is the wrong place for that: your listener is the right one.
 * If the attributes are missing, this answers 500, which is the honest status for
 * "the application was assembled wrong".
 *
 * It also calls flushBuffer() on the way out, on purpose. That commits the response: the
 * status line and the headers are already on the wire. Anything a filter tries to add to the
 * response after the chain returns from here is too late, and Tomcat drops it silently.
 */
public class InfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain;charset=UTF-8");
        ServletContext app = req.getServletContext();
        Object name = app.getAttribute("appName");
        Object started = app.getAttribute("startedAt");
        if (!(name instanceof String n) || !(started instanceof Instant at)) {
            res.setStatus(500);
            res.getWriter().print("application scope is empty: did the listener run?");
            res.flushBuffer();
            return;
        }
        res.setStatus(200);
        res.getWriter().print(n + " " + at);
        res.flushBuffer();
    }
}
