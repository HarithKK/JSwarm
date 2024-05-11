package org.usa.soc.si.mfa;

import org.usa.soc.core.ds.Vector;

public class Moth {

    private Vector position;

    private double fitnessValue;
    public Moth(Vector position) {
        this.position = position;
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position);
    }


    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }
}
