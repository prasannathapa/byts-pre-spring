package state;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Given to you. One log, shared by every request thread, so it has to be thread safe.
 *
 * CopyOnWriteArrayList is the right shape here and a terrible shape almost everywhere else:
 * every add copies the whole array. Reads are free and never throw
 * ConcurrentModificationException, writes are rare, the list stays small. That is the one
 * case it was built for.
 */
public class RequestLog {

    private final List<String> lines = new CopyOnWriteArrayList<>();
    private final List<Long> millis = new CopyOnWriteArrayList<>();

    /** line is "METHOD /path -> status"; ms is how long the chain took. */
    public void add(String line, long ms) {
        lines.add(line);
        millis.add(ms);
    }

    public List<String> lines() {
        return List.copyOf(lines);
    }

    public List<Long> millis() {
        return List.copyOf(millis);
    }

    public void clear() {
        lines.clear();
        millis.clear();
    }
}
