package state;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * One filter, mapped at /*, doing the two jobs every real filter does: it decorates the
 * response on the way in, and it records what happened on the way out.
 *
 * What it must do:
 *
 *   1. Give every response a fresh X-Request-Id header, BEFORE calling the chain.
 *      UUID.randomUUID().toString() is fine. Set it afterwards and you are racing the
 *      commit: once the response is committed, setHeader is a no-op and nobody tells you.
 *
 *   2. If the path starts with /admin and there is no X-Token request header, answer 401
 *      and do NOT call the chain. That is a short circuit: the servlet never runs. Any
 *      other path, and any /admin path that does carry the header, goes through.
 *
 *   3. Log exactly one line per request, "METHOD /path -> status", in a finally block, with
 *      the elapsed milliseconds as the second argument to log.add. The finally matters: the
 *      401 you send yourself has to be logged too, and so does a request that ends in an
 *      exception. Read the status back off the response with res.getStatus() AFTER the chain,
 *      because that is when it is finally known.
 *
 * Keep the timing in the log. Do not try to put it in a response header on the way out.
 */
public class RequestLogFilter extends HttpFilter {

    private final RequestLog log;

    public RequestLogFilter(RequestLog log) {
        this.log = log;                      // handed in by Server: the filter finds nothing itself
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // TODO: t0 = System.nanoTime(), set X-Request-Id, then try { guard /admin, else
        //       chain.doFilter(req, res); } finally { log.add(method + " " + uri + " -> " + status, ms); }
        throw new UnsupportedOperationException("TODO: RequestLogFilter.doFilter");
    }
}
