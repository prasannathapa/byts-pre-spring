package finale;

import java.util.List;

/**
 * A CSV statement.  (principles: OCP, LSP)
 *
 * TODO: return a header line "id,balance", then one "id,balance" row per account,
 *       all joined by newlines.
 *
 * Example for accounts [A=500, B=0]:
 *     id,balance
 *     A,500
 *     B,0
 *
 * No accounts still gets the header, and nothing after it.
 */
public final class CsvFormat implements StatementFormat {
    @Override
    public String format(List<Account> accounts) {
        // TODO: header "id,balance", then one row per account, joined by "\n".
        throw new UnsupportedOperationException("TODO: CsvFormat.format(accounts)");
    }
}
