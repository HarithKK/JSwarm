package org.usa.soc.core;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbsAgent implements Cloneable {

    private UUID id = UUID.randomUUID();

    private int index;
    protected Vector position = null;
    public double[] minBoundary = null, maxBoundary = null;
    public int numberOfDimensions = 0;
    private Set<AbsAgent> conncetions = new HashSet<>();
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
        if(!getConncetions().contains(agent)){
            getConncetions().add(agent);
        }
        return this;
    }

    public void randPosition(){ position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1); }

    public UUID getId() {
        return id;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Set<AbsAgent> getConncetions() {
        return conncetions;
    }

    public void setConncetions(Set<AbsAgent> conncetions) {
        this.conncetions = conncetions;
    }
}
