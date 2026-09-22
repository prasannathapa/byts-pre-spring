package report;

// The policy. It does NOT know or care which concrete format it uses.
// The format is injected through the constructor (dependency inversion): the field is already
// wired for you. Your job is to USE it - do not write `new CsvFormat()` in here.
public class ReportService {

    private final ReportFormat format;

    public ReportService(ReportFormat format) {
        this.format = format;
    }

    // TODO:
    //   1. Reject a null or blank title by throwing IllegalArgumentException.
    //      (Validation is the SERVICE's job, not the formatter's - single responsibility.)
    //   2. Otherwise, return the injected format's output for this report.
    public String export(Report report) {
        throw new UnsupportedOperationException("TODO: validate the title, then use the injected format");
    }
}
