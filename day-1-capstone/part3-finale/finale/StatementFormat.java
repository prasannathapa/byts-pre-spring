package finale;

import java.util.List;

/**
 * The seam.  (principles: ISP + OCP + DIP)
 *
 * GIVEN - you do not edit this. One tiny, role-shaped interface (ISP). A new
 * output format is a new class that implements it (OCP), and the exporter depends
 * only on this promise, never on a concrete format (DIP). It is the exact idea
 * from part 2's ReportFormat, now pointed at a list of accounts.
 */
@FunctionalInterface
public interface StatementFormat {
    String format(List<Account> accounts);
}
