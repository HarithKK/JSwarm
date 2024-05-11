package org.usa.soc.si.mfa;

import java.util.Comparator;

public class FlameComparator implements Comparator<Flame> {
    @Override
    public int compare(Flame o1, Flame o2) {
        if(o1.getFitnessValue() > o2.getFitnessValue())
            return 1;
        else if (o1.getFitnessValue() == o2.getFitnessValue()) {
            return 0;
        }
        return -1;
    }
}

