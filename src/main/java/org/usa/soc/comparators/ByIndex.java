package org.usa.soc.comparators;

import org.usa.soc.core.AbsAgent;

import java.util.Comparator;

public class ByIndex implements Comparator<AbsAgent> {
    @Override
    public int compare(AbsAgent o1, AbsAgent o2) {
        if(o1.getIndex() < o2.getIndex()){
            return -1;
        }else{
            return 1;
        }
    }
}
