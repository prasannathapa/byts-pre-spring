package wrappers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A least-recently-used cache in about four lines of real code.
 *
 * LinkedHashMap keeps a doubly linked list through its entries. By default that list is
 * in INSERTION order. Pass true as the third constructor argument and it becomes ACCESS
 * order: every get and every put moves that entry to the end of the list, so the head is
 * always the one nobody has touched for longest.
 *
 * Then removeEldestEntry. LinkedHashMap calls it after every insertion and hands you the
 * oldest entry; say true and it is dropped. It is documented as an extension point, which
 * is the difference between this subclass and the one in InstrumentedHashSet: here the
 * superclass PROMISES to call you back, in writing. Item 19 in one sentence: design and
 * document for inheritance, or forbid it.
 *
 * This is the one place all day where extending a collection is the right answer.
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    public LruCache(int capacity) {
        // TODO: the third argument to LinkedHashMap's constructor is accessOrder, and it
        //       is wrong below. Fix it, keep the capacity, and delete the throw.
        super(16, 0.75f, false);
        this.capacity = capacity;
        throw new UnsupportedOperationException("TODO: LruCache(capacity)");
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // TODO: return true when the map has grown past its capacity. Use the capacity
        //       field: a number typed in here is not a cache, it is a coincidence.
        throw new UnsupportedOperationException("TODO: removeEldestEntry");
    }
}
