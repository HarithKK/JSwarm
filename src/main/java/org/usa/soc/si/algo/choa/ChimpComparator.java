package org.usa.soc.si.algo.choa;

import org.usa.soc.util.Validator;

import java.util.Comparator;

public class ChimpComparator implements Comparator<Chimp> {

    boolean isGlobalMinima = false;

    public ChimpComparator(boolean b){
        this.isGlobalMinima = b;
    }
    @Override
    public int compare(Chimp o1, Chimp o2) {
        if(Validator.validateBestValue(o1.getFitnessValue(), o2.getFitnessValue(), isGlobalMinima)){
            return 1;
        }else if (o1.getFitnessValue() == o2.getFitnessValue()) {
            return 0;
        }
        return -1;
    }
}
