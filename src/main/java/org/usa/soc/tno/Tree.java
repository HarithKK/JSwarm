package org.usa.soc.tno;

import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class Tree {

    private Vector position;
    double[] minBoundary, maxBoundary;
    int numberOfDimensions;

    public Tree(double[] minBoundary, double[] maxBoundary, int numberOfDimensions){

        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;

        this.numberOfDimensions = numberOfDimensions;

        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
    }

    public Vector getPosition(){
       return this.position.getClonedVector();
    }

    public void setVector(Vector v){
        this.position.setVector(v);
    }

}
