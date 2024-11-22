package org.usa.soc.comparators;

import org.usa.soc.core.AbsAgent;

import java.util.Comparator;
import java.util.LinkedList;

public class ParetoComparator<T> extends LinkedList<Comparator<T>> implements Comparator<T> {

    public int compare(T a, T b) {
        int reference = 0;
        for (Comparator<T> comparator : this) {
            if (reference == 0) {
                reference = (int) Math.signum(comparator.compare(a, b));
            } else {
                int comparison = (int) Math.signum(comparator.compare(a, b));
                if (comparison * reference < 0) {
                    return 0;
                }
            }
        }
        return reference;
    }
}

