package org.usa.soc.si.algo.tsoa;

import java.util.Comparator;

public class TreeComparator implements Comparator<Tree> {
    @Override
    public int compare(Tree o1, Tree o2) {
        if(o1.getFitnessValue() > o2.getFitnessValue())
            return 1;
        else if (o1.getFitnessValue() == o2.getFitnessValue()) {
            return 0;
        }
        return -1;
    }
}
