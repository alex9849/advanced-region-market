package net.alex9849.arm.util;

import java.util.Iterator;

public class ConcatedIterator<T> implements Iterator<T> {
    private final Iterator<T>[] iterators;
    private int currentIndex = 0;

    public ConcatedIterator(Iterator<T>... iterators) {
        this.iterators = iterators;
    }

    @Override
    public boolean hasNext() {
        while (currentIndex < iterators.length && !iterators[currentIndex].hasNext()) {
            currentIndex++;
        }
        return currentIndex < iterators.length;
    }

    @Override
    public T next() {
        while (!iterators[currentIndex].hasNext() && currentIndex < iterators.length - 1) {
            currentIndex++;
        }
        return iterators[currentIndex].next();
    }
}
