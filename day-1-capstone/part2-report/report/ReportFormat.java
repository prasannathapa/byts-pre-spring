package report;

// The seam. One small, role-shaped interface: a thing that can turn a Report into text.
// This is the whole point of the exercise - everything depends on THIS, not on a concrete format.
// (Interface segregation: one method. Functional, so a test can supply a format with a lambda.)
@FunctionalInterface
public interface ReportFormat {
    String format(Report report);
}
