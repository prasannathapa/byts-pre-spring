package wrappers;

import java.util.Collection;
import java.util.HashSet;

/**
 * The broken version, kept on purpose. Nothing to do in this file.
 *
 * It looks right. It counts every add, and addAll adds a batch so it counts the batch.
 * Then you call addAll with three elements and getAddCount says six, because HashSet's
 * own addAll is written as a loop over add, and add here is YOUR add. The superclass
 * called back into the subclass and nobody told you.
 *
 * This is Effective Java Item 18, and the fix is not to delete the count in addAll (that
 * only works until HashSet changes its mind about how addAll is implemented). The fix is
 * InstrumentedSet: stop extending, start wrapping.
 *
 * A test in RunTests pins the 6 so the bug is documented rather than mysterious.
 */
public class InstrumentedHashSet<E> extends HashSet<E> {

    private int addCount = 0;

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);          // and super.addAll loops back into add, above
    }

    public int getAddCount() {
        return addCount;
    }
}
