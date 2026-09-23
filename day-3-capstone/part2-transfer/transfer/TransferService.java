package transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Money leaves one account and arrives in another, or neither thing happens.
 *
 * Two UPDATEs, one transaction. The shape below is the shape of every transaction
 * method you will ever write, so learn it once:
 *
 *     setAutoCommit(false)
 *     try      { do the work; commit(); }
 *     catch    { rollback(); throw something the caller understands; }
 *     finally  { setAutoCommit(true); }
 *
 * Three details decide whether this is correct or merely looks correct.
 *
 *  1. The catch has to be wider than SQLException. If you throw an
 *     IllegalArgumentException from inside the try and only catch SQLException, it
 *     sails past your rollback, hits the finally, and setAutoCommit(true) COMMITS
 *     the half-finished transfer. One test in RunTests does exactly that to you.
 *  2. executeUpdate returns a count. An UPDATE of an id that is not there succeeds
 *     happily and changes 0 rows. If you do not read the count, nobody does.
 *  3. The arithmetic belongs in SQL: balance_cents = balance_cents - ?. Read the
 *     balance into Java first and two threads will read the same number.
 */
public final class TransferService {

    /** Adds cents (which may be negative) to one account. Both methods want this. */
    private static final String MOVE = "UPDATE account SET balance_cents = balance_cents + ? WHERE id = ?";

    private TransferService() { }

    public static void transfer(Connection c, long from, long to, long cents) throws SQLException {
        // TODO:
        //   1. Before you touch the database: reject cents that are not positive, and
        //      reject a transfer from an account to itself. Both are IllegalArgumentException.
        //      A test calls you with a CLOSED connection to prove you checked first.
        //   2. setAutoCommit(false), then run MOVE twice: minus cents on `from`, plus
        //      cents on `to`. Check each returned count is 1, or throw
        //      IllegalArgumentException naming the id that is not there.
        //   3. commit() when both worked.
        //   4. On a SQLIntegrityConstraintViolationException the CHECK fired, which
        //      means the debit would have gone below zero: rollback, then throw
        //      new InsufficientFundsException(from, to, cents).
        //   5. On ANY other failure: rollback and let it out.
        //   6. finally: setAutoCommit(true), always. A pooled connection goes back to
        //      the pool with whatever setting you left on it.
        throw new UnsupportedOperationException("TODO: TransferService.transfer(connection, from, to, cents)");
    }

    /** One atomic UPDATE. No SELECT, no transaction of your own: the statement is the transaction. */
    public static void deposit(Connection c, long id, long cents) throws SQLException {
        // TODO: reject a non-positive amount, then run MOVE once with a positive amount.
        //       If it changed no rows, throw IllegalArgumentException naming the id.
        //       Twenty threads run this at once in the last test. Read the balance into a
        //       Java variable first and you will lose most of the deposits.
        throw new UnsupportedOperationException("TODO: TransferService.deposit(connection, id, cents)");
    }

    /** Runs MOVE and returns how many rows it changed. Yours to use, or to ignore. */
    private static int move(Connection c, long id, long delta) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(MOVE)) {
            ps.setLong(1, delta);
            ps.setLong(2, id);
            return ps.executeUpdate();
        }
    }
}
