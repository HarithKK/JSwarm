package org.usa.soc.si.avoa;

import java.util.Comparator;

public class VultureComparator implements Comparator<Vulture> {
    @Override
    public int compare(Vulture o1, Vulture o2) {
        if(o1.getFitnessValue() > o2.getFitnessValue())
            return 1;
        else if (o1.getFitnessValue() == o2.getFitnessValue()) {
            return 0;
        }
        return -1;
    }
}