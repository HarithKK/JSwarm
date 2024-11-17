package org.usa.soc.util;

import org.usa.soc.comparators.ParetoComparator;
import org.usa.soc.core.AbsAgent;

import java.util.Collection;
import java.util.HashSet;

public class ParetoUtils {
    private interface OrderChecker<Object> {
        public boolean canOrderAs(Object i1, Object i2);
    }

    public static <Object> Collection<Object> getMinimalFrontierOf(
            final Collection<Object> population,
            final ParetoComparator<Object> comparator) {
        OrderChecker<Object> checker = new OrderChecker<Object>() {

            public boolean canOrderAs(Object i1, Object i2) {
                return comparator.compare(i1, i2) > 0;
            }
        };
        return getFrontierOf(population, checker);
    }

    private static <Individual> Collection<Individual> getFrontierOf(
            final Collection<Individual> population,
            OrderChecker<Individual> checker) {
        Collection<Individual> frontier = new HashSet<Individual>();
        for (Individual i1 : population) {
            Boolean add = true;
            for (Individual i2 : population) {
                if (checker.canOrderAs(i1, i2)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                frontier.add(i1);
            }
        }
        return frontier;
    }

}
