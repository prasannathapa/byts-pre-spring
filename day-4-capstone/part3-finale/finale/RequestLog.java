package finale;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Given, straight from part 2: one log, every thread, no surprises. */
public class RequestLog {

    private final List<String> lines = new CopyOnWriteArrayList<>();

    public void add(String line) {
        lines.add(line);
    }

    public List<String> lines() {
        return List.copyOf(lines);
    }

    public void clear() {
        lines.clear();
    }
}
