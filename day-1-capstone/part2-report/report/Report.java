package report;

import java.util.List;

// The data to be exported. Plain, immutable value (a record). You do not need to change this.
public record Report(String title, List<String> rows) {
}
