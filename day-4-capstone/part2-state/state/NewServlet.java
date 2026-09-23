package state;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Given to you: three servlets that show the difference between a redirect and a forward.
 *
 *   /new     prints what it can see about the request it is serving
 *   /old     sends a 302 to /new. That is an instruction to the CLIENT: go and ask again.
 *            Two requests, two log lines, and the address bar changes.
 *   /alias   forwards to /new INSIDE the container. One request, one log line, and the
 *            client never learns that /new was involved. getRequestURI() now says /new,
 *            and the original path is parked in the forward attributes.
 */
public class NewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain;charset=UTF-8");
        res.getWriter().print("new uri=" + req.getRequestURI()
                + " from=" + req.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI)
                + " disp=" + req.getDispatcherType());
    }
}

class OldServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect("/new");
    }
}

class AliasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/new").forward(req, res);
    }
}
