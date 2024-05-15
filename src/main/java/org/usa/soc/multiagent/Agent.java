package org.usa.soc.multiagent;

import org.usa.soc.core.ds.Margins;

public abstract class Agent implements Cloneable{

    private double x = 0;
    private double y = 0;

    public void updatePosition(double x, double y){
        this.setX(x);
        this.setY(y);
    }



    public abstract void step();

    public void checkMargins(Margins m) {
        if(this.getX() > m.xMax) { this.setX(m.xMax); }
        if(this.getX() < m.xMin) { this.setX(m.xMin); }
        if(this.getY() > m.yMax) { this.setY(m.yMax); }
        if(this.getY() < m.yMax) { this.setY(m.yMin); }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public Agent clone() {
        try {
            Agent clone = (Agent) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
