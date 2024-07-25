package org.usa.soc.core;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public abstract class AbsAgent implements Cloneable {
    protected Vector position = null;
    public double[] minBoundary = null, maxBoundary = null;
    public int numberOfDimensions = 0;

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector p) { position.setVector(p);}

    public void randPosition(){ position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary); }
}
