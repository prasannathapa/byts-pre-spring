package hello;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The same job again, in JSON this time, so you see the two halves of every reply:
 * a status line and a content type on one side, bytes on the other.
 *
 * GET /square?n=7   ->  200, application/json, exactly {"n":7,"square":49}
 * GET /square?n=-3  ->  200, {"n":-3,"square":9}
 * GET /square?n=x   ->  400, application/json, {"error":"n must be an integer"}
 * GET /square       ->  400, the same error object
 *
 * Note what the error is NOT: it is not Tomcat's HTML page, and it is not a 500 from an
 * uncaught NumberFormatException. Bad input from a client is a 400 that the client can
 * parse. Use Json.str for the message so the quoting is right.
 */
public class SquareServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // TODO: read n, parse it with Long.parseLong inside a try.
        //       On success: 200, application/json, {"n":<n>,"square":<n*n>}  (no spaces).
        //       On null, blank or unparsable input: 400 and
        //       {"error":"n must be an integer"} built with Json.str.
        throw new UnsupportedOperationException("TODO: SquareServlet.doGet");
    }
}
