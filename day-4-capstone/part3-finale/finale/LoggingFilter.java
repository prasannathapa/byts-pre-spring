package finale;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Given, the part 2 idiom. Notice what it does not do: it does not look anything up, and it
 * has no idea who is logging. It is handed a RequestLog and it uses that one.
 *
 * Logging is a cross-cutting concern, so it is wired once at the root and never mentioned
 * again in any servlet. That is the sentence to remember when Spring's container turns up.
 */
public class LoggingFilter extends HttpFilter {

    private final RequestLog log;

    public LoggingFilter(RequestLog log) {
        this.log = log;
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } finally {
            log.add(req.getMethod() + " " + req.getRequestURI() + " -> " + res.getStatus());
        }
    }
}
