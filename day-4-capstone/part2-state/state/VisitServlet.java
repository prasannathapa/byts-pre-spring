package state;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /visit  ->  "visit 1", then "visit 2", then "visit 3" for the same visitor.
 *
 * Per visitor, not per server. The count lives in the HttpSession, which the container finds
 * from the JSESSIONID cookie the browser sends back. A client that keeps cookies keeps
 * counting; a client that drops them is a stranger every time and sees "visit 1" forever.
 * Both of those are tests.
 *
 * There is exactly one HitServlet object and one VisitServlet object for the whole
 * application, shared by every thread, so an instance field here would count everybody's
 * visits into one number. Session state goes in the session.
 */
public class VisitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // TODO: req.getSession(), read the "visits" attribute (it is null the first time),
        //       add one, write it back, and print "visit " + n. No instance fields.
        throw new UnsupportedOperationException("TODO: VisitServlet.doGet");
    }
}
