package finale;

import java.util.List;

/**
 * The policy that ties it all together.  (principles: DIP + composition)
 *
 * It holds a StatementFormat that is INJECTED through the constructor (DIP) - it
 * never says `new CsvFormat()` itself. It works over a list of accounts it is
 * handed (composition). Swap the format, and the same exporter produces a
 * different output with no change here (OCP).
 *
 * TODO: store the injected format, and in export() delegate to it.
 */
public final class StatementExporter {

    private final StatementFormat format;

    public StatementExporter(StatementFormat format) {
        // TODO: store the injected format.
        throw new UnsupportedOperationException("TODO: StatementExporter(format)");
    }

    public String export(List<Account> accounts) {
        // TODO: delegate to the injected format. Do NOT switch on a format type here.
        throw new UnsupportedOperationException("TODO: export(accounts)");
    }
}
