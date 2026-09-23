package wrappers;

import java.util.Collection;
import java.util.Set;

/**
 * The same counting set, done by wrapping instead of extending.
 *
 * It extends ForwardingSet, so every method it does not override goes to the set you
 * handed the constructor. Override the two you care about, count, then call super.
 * super.add and super.addAll forward OUT to the wrapped set, and the wrapped set's
 * internal calls can never come back in here. Three adds, count of three.
 *
 * The other prize: this works over a HashSet, a TreeSet, a LinkedHashSet, anything
 * that is a Set. You are not married to one implementation.
 */
public class InstrumentedSet<E> extends ForwardingSet<E> {

    private int addCount = 0;

    public InstrumentedSet(Set<E> wrapped) {
        super(wrapped);
    }

    @Override
    public boolean add(E e) {
        // TODO: count the attempt, then let the superclass forward it on.
        //       An add that returns false because the element was already there still
        //       counts: we are counting attempts, not new elements.
        throw new UnsupportedOperationException("TODO: InstrumentedSet.add");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO: count how many elements were offered, then forward the whole collection.
        //       Count the size of c, not a number you picked.
        throw new UnsupportedOperationException("TODO: InstrumentedSet.addAll");
    }

    public int getAddCount() {
        throw new UnsupportedOperationException("TODO: getAddCount");
    }
}
