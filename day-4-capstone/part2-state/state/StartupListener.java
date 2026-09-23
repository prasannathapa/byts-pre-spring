package state;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Application scope, published once, before the first request.
 *
 * contextInitialized runs while the context starts, on the thread that starts it. Anything you
 * put in the ServletContext here is visible to every servlet, on every thread, for the life of
 * the application. That is the third scope: request, session, application.
 *
 * Publish two attributes:
 *   "appName"   the String "byts-bank"
 *   "startedAt" an Instant, Instant.now()
 *
 * InfoServlet reads them and answers 500 if they are not there, so a listener that publishes
 * nothing, or publishes in contextDestroyed instead, shows up as a failing test rather than
 * as a mystery.
 *
 * Write this one FIRST. While it still throws, the context refuses to start at all and every
 * other test in this part fails with a 404 and a stack trace at the top of the run. That is
 * not a bug in the tests: a listener that throws takes the whole application down with it,
 * and seeing that once is worth the confusion.
 */
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // TODO: event.getServletContext().setAttribute(...) twice.
        throw new UnsupportedOperationException("TODO: StartupListener.contextInitialized");
    }
}
