package org.usa.soc.si.algo.ssa;

import org.usa.soc.util.Validator;

import java.util.Comparator;

public class SquirrelComparator implements Comparator<Squirrel> {

    boolean isGlobalMinima = false;

    public SquirrelComparator(boolean b){
        this.isGlobalMinima = b;
    }
    @Override
    public int compare(Squirrel o1, Squirrel o2) {
        if(Validator.validateBestValue(o1.getFitnessValue(), o2.getFitnessValue(), isGlobalMinima)){
            return 1;
        }else if (o1.getFitnessValue() == o2.getFitnessValue()) {
            return 0;
        }
        return -1;
    }
}
