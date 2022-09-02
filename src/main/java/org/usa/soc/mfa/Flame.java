package org.usa.soc.mfa;

import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class Flame {

    private Vector position;
    private double fitnessValue;

    public Flame(Vector flamePosition) {
        this.position = flamePosition;
    }

    public Vector getPosition() {
        return position;
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
