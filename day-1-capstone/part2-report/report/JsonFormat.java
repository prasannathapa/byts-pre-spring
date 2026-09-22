package report;

// TODO: a JSON format. Produce exactly this shape (no spaces):
//   {"title":"<title>","rows":["row0","row1"]}
// An empty report has an empty array: {"title":"Empty","rows":[]}
// A StringJoiner("," , "[", "]") makes the rows array easy. Assume titles and rows have no quotes.
public final class JsonFormat implements ReportFormat {
    @Override
    public String format(Report report) {
        throw new UnsupportedOperationException("TODO: implement JSON formatting");
    }
}
