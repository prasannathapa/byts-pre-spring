package finale;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * The edge of the application. Everything above it speaks Java; everything below it speaks
 * HTTP. This class is the translator and nothing else.
 *
 * It is handed what it needs and it constructs NOTHING: no DataSource, no repository, no
 * renderer. If you find yourself typing `new` in here for anything other than a String,
 * stop: the wiring belongs in App.
 *
 * Mapped at /accounts/*, so getPathInfo() is the bit after /accounts.
 *
 *   GET  /accounts            pathInfo null or "/"   200 application/json
 *                             [{"id":1,"owner":"ann","balancePaise":10000},...]
 *                             Build it with Json.account and commas between, no trailing comma.
 *   GET  /accounts/1          200 application/json   {"id":1,"owner":"ann","balancePaise":10000}
 *   GET  /accounts/999        404 application/json   {"error":"no account 999"}
 *                             Get there with service.find(id).orElseThrow(
 *                                 () -> new NoSuchElementException("no account " + id))
 *   GET  /accounts/abc        400 application/json   {"error":"id must be a number"}
 *   GET  /accounts/report     200 text/plain         whatever reports.get().render() returns
 *   POST /accounts/transfer   form fields from, to, amount (they arrive as strings)
 *                             200 application/json   {"ok":true}
 *                             400 on a bad number or a rule the service refuses
 *                             404 when an account does not exist
 *                             409 when the money is not there
 *                             the body of every refusal is Json.error(exception message)
 *   anything else             404 with a JSON error
 *
 * The three catch blocks are the whole lesson about layers:
 *   IllegalArgumentException      -> 400   (NumberFormatException is one of these)
 *   NoSuchElementException        -> 404
 *   InsufficientFundsException    -> 409
 * The service threw domain exceptions. Only this class knows they have numbers.
 */
public class AccountServlet extends HttpServlet {

    // TODO: two final fields, an AccountService and a Supplier<ReportRenderer>, and a
    //       constructor that takes both. The Supplier is there so the renderer is built on
    //       first use, not at boot: call reports.get() only when someone asks for a report.
    public AccountServlet(AccountService service, Supplier<ReportRenderer> reports) {
        throw new UnsupportedOperationException("TODO: AccountServlet(service, reports)");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // TODO: switch on getPathInfo(): the list, the report, or one account by id.
        throw new UnsupportedOperationException("TODO: AccountServlet.doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // TODO: only /transfer. Read from, to and amount, hand them to the service, and
        //       turn each exception it throws into the status the table above gives it.
        throw new UnsupportedOperationException("TODO: AccountServlet.doPost");
    }
}
