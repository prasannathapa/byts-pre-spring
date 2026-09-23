package wrappers;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * A Set that owns another Set and passes every call straight through to it.
 * (Effective Java Item 18: favour composition over inheritance.)
 *
 * This is the boring half of the Decorator pattern and there is no clever way to write
 * it: you type out every method of the interface and forward it. Half of them are done
 * for you so you can see the shape. Finish the rest.
 *
 * Why bother, when `extends HashSet` is one line? Because a subclass can see its
 * superclass calling its own methods. HashSet.addAll happens to call add for each
 * element, so a subclass that counts both counts twice. A wrapper cannot see inside
 * the set it wraps, so it cannot be surprised by it. It also works over ANY Set: hand
 * it a TreeSet, a LinkedHashSet, something you write next year. A subclass of HashSet
 * is a subclass of HashSet forever.
 */
public class ForwardingSet<E> implements Set<E> {

    private final Set<E> wrapped;

    public ForwardingSet(Set<E> wrapped) {
        this.wrapped = wrapped;
    }

    // --- given: the easy forwards ------------------------------------------------
    @Override public int size()                  { return wrapped.size(); }
    @Override public boolean isEmpty()           { return wrapped.isEmpty(); }
    @Override public boolean contains(Object o)  { return wrapped.contains(o); }
    @Override public Iterator<E> iterator()      { return wrapped.iterator(); }
    @Override public Object[] toArray()          { return wrapped.toArray(); }
    @Override public <T> T[] toArray(T[] a)      { return wrapped.toArray(a); }
    @Override public boolean add(E e)            { return wrapped.add(e); }
    @Override public boolean remove(Object o)    { return wrapped.remove(o); }
    @Override public void clear()                { wrapped.clear(); }
    @Override public String toString()           { return wrapped.toString(); }

    // --- yours: the rest of the boilerplate --------------------------------------

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("TODO: forward containsAll");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO: forward it. Forward it to the WRAPPED set, in one call. Do not loop
        //       calling this.add: that is the self-use bug you are here to avoid.
        throw new UnsupportedOperationException("TODO: forward addAll");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("TODO: forward removeAll");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO: retainAll keeps what IS in c. removeAll drops what is in c. Not the same method.
        throw new UnsupportedOperationException("TODO: forward retainAll");
    }

    @Override
    public boolean equals(Object o) {
        // TODO: a Set is equal to any Set with the same elements. Ask the wrapped set,
        //       do not compare references.
        throw new UnsupportedOperationException("TODO: forward equals");
    }

    @Override
    public int hashCode() {
        // TODO: equal sets must hash alike, so this has to agree with equals above.
        throw new UnsupportedOperationException("TODO: forward hashCode");
    }
}
