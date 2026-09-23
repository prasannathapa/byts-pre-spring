package hello;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /hello?name=byts  ->  200, text/plain, body "hello byts"
 * GET /hello            ->  400, body "name required"
 * GET /hello?name=%20   ->  400, body "name required"      (blank is not a name)
 * POST /hello           ->  405, and you write no code at all for that
 *
 * Two things the tests are strict about.
 *
 * First, the charset. Set "text/plain;charset=UTF-8" BEFORE you ask for the writer.
 * A name like Zoe with a diaeresis goes out in whatever encoding the response settled on,
 * and the default is not UTF-8. Set it late and you are writing through a writer that has
 * already picked.
 *
 * Second, setStatus versus sendError. sendError hands the request back to the container,
 * which replaces your body with its own HTML error page. The test reads the body.
 */
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // TODO: read the name parameter with req.getParameter("name").
        //       Missing (null) or blank -> status 400 and the body "name required".
        //       Otherwise -> status 200 and the body "hello " + name.
        //       Set the content type, with the charset, in both cases.
        throw new UnsupportedOperationException("TODO: HelloServlet.doGet");
    }
}
