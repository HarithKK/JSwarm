package org.usa.soc.choa;

import org.usa.soc.util.Validator;

import java.util.Comparator;

public class ChimpComparator implements Comparator<Chimp> {

    boolean isLocalMinima = false;

    public ChimpComparator(boolean b){
        this.isLocalMinima = b;
    }
    @Override
    public int compare(Chimp o1, Chimp o2) {
        if(Validator.validateBestValue(o1.getFitnessValue(), o2.getFitnessValue(), isLocalMinima)){
            return 1;
        }else if (o1.getFitnessValue() == o2.getFitnessValue()) {
            return 0;
        }
        return -1;
    }
}
