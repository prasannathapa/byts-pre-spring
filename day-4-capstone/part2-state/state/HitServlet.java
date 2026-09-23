package state;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * This one is written for you, and it is WRONG. Your job is to fix it.
 *
 * GET /hits            counts one hit and prints "hits N" with the new count
 * GET /hits?reset=true puts the counter back to zero and prints "hits 0"
 *
 * One servlet object serves every request, and Tomcat will happily run fifty requests through
 * it at the same time on fifty different threads. Read, pause, write is three steps, and two
 * threads that interleave them lose an increment between them. The onSpinWait below just
 * widens a window that is already open: take it out and the bug becomes rare rather than gone,
 * which is worse, because rare bugs reach production.
 *
 * The test fires 50 threads x 20 requests three times over and demands that the 1000 answers
 * are 1 to 1000 with nothing repeated and nothing missing.
 *
 * Fix it with java.util.concurrent.atomic.AtomicInteger: incrementAndGet is one atomic step,
 * and it gives you back the value your own request produced. (synchronized on the method also
 * works and is slower; the point is that "int++" is not one step.)
 */
public class HitServlet extends HttpServlet {

    private int hits;                                   // TODO: this field is the bug

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain;charset=UTF-8");
        if ("true".equals(req.getParameter("reset"))) {
            hits = 0;                                   // TODO: reset the atomic counter instead
            res.getWriter().print("hits 0");
            return;
        }
        int h = hits;                                   // TODO: replace these three lines with
        Thread.onSpinWait();                            //       one incrementAndGet, and print
        hits = h + 1;                                   //       the value it returns
        res.getWriter().print("hits " + (h + 1));
    }
}
