package org.usa.soc.comparators;

import org.usa.soc.core.AbsAgent;

import java.util.Comparator;
import java.util.LinkedList;

public class ParetoComparator<AbsAgent> extends LinkedList<Comparator<AbsAgent>> implements Comparator<AbsAgent> {

    public int compare(AbsAgent a, AbsAgent b) {
        int reference = 0;
        for (Comparator<AbsAgent> comparator : this) {
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

