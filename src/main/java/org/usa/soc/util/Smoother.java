package org.usa.soc.util;

import org.usa.soc.core.ds.Vector;

public class Smoother {
    public static Vector smooth(Vector input, Double magnitude){
        Vector tmpV = input.getClonedVector();
        Double tmpMag = 0.0;
        for (Double d: tmpV.getPositionIndexes()) {
            tmpMag += Math.pow(d, 2);
        }
        tmpMag = Math.sqrt(tmpMag);

        if(tmpMag > magnitude){
            for(int i=0;i< tmpV.getNumberOfDimensions(); i++){
                tmpV.setValue(tmpV.getValue(i) / tmpMag, i);
            }
        }

        return tmpV;
    }
}
