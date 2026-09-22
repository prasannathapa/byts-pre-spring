package finale;

import java.util.List;

/**
 * A plain-text statement: one line per account.  (principles: OCP, LSP)
 *
 * TODO: return each account as "id: balance" on its own line, in order, joined
 *       by newlines. Balance is in cents; print the raw cent count.
 *
 * Example for accounts [A=500, B=0]:
 *     A: 500
 *     B: 0
 *
 * No accounts means no lines: return an empty string.
 */
public final class PlainFormat implements StatementFormat {
    @Override
    public String format(List<Account> accounts) {
        // TODO: build one "id: balance" line per account, joined by "\n".
        throw new UnsupportedOperationException("TODO: PlainFormat.format(accounts)");
    }
}
