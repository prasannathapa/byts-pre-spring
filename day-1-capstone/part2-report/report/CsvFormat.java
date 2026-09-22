package report;

// TODO: a comma-separated-values format.
//   - The title goes on its own first line.
//   - Then each row goes on its own line.
//   - An empty report (no rows) is just the title, with no trailing newline.
//   String.join("\n", rows) will help.
public final class CsvFormat implements ReportFormat {
    @Override
    public String format(Report report) {
        throw new UnsupportedOperationException("TODO: implement CSV formatting");
    }
}
