package org.usa.soc.core;

import org.apache.commons.math3.analysis.function.Abs;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbsAgent implements Cloneable {

    private UUID id = UUID.randomUUID();
    protected Vector position = null;
    public double[] minBoundary = null, maxBoundary = null;
    public int numberOfDimensions = 0;
    public Set<AbsAgent> conncetions = new HashSet<>();
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector p) {
        if(position == null){
            position = p;
        }else{
            position.setVector(p);
        }

    }

    public AbsAgent addConnection(AbsAgent agent){
        if(!conncetions.contains(agent)){
            conncetions.add(agent);
        }
        return this;
    }

    public void randPosition(){ position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary); }

    public UUID getId() {
        return id;
    }
}
