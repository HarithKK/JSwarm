package org.usa.soc.ssa;

import org.usa.soc.util.Validator;

import java.util.Comparator;

public class SquirrelComparator implements Comparator<Squirrel> {

    boolean isLocalMinima = false;

    public SquirrelComparator(boolean b){
        this.isLocalMinima = b;
    }
    @Override
    public int compare(Squirrel o1, Squirrel o2) {
        if(Validator.validateBestValue(o1.getFitnessValue(), o2.getFitnessValue(), isLocalMinima)){
            return 1;
        }else if (o1.getFitnessValue() == o2.getFitnessValue()) {
            return 0;
        }
        return -1;
    }
}
