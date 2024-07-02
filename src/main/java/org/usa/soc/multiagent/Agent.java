package org.usa.soc.multiagent;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

import java.util.List;

public abstract class Agent implements Cloneable{

    private Vector position;

    private double[] xm = new double[]{0,1}, ym=new double[]{0,1};
    private Margins margines;

    public void initPosition(Margins m){
        if(m != null){
            this.xm = new double[]{m.xMin, m.yMin};
            this.ym = new double[]{m.xMax, m.yMax};
            setMargines(m);
        }
        this.setPosition(Randoms.getRandomVector(2, xm, ym));
    }

    public void initPosition(Margins m, double x, double y){
        if(m != null){
            this.xm = new double[]{m.xMin, m.yMin};
            this.ym = new double[]{m.xMax, m.yMax};
            setMargines(m);
        }
        this.setPosition(Randoms.getRandomVector(2, xm, ym));
        this.updatePosition(x, y);
    }

    public void updatePosition(double x, double y){
        this.setX(x);
        this.setY(y);
    }

    public void updatePosition(Vector v){
        this.position.setVector(v.getClonedVector());
    }

    public abstract void step();

    public double getX() {
        return this.getPosition().getValue(0);
    }

    public void setX(double x) {
        this.getPosition().setValue(x, 0);
    }

    public double getY() {
        return this.getPosition().getValue(1);
    }

    public void setY(double y) {
        this.getPosition().setValue(y, 1);
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

    public Agent getInitialAgent(double x, double y) {
        this.setX(x);
        this.setY(y);
        return this;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Margins getMargines() {
        return margines;
    }

    public void setMargines(Margins margines) {
        this.margines = margines;
    }

    @Override
    public String toString(){
        return "[x]="+getX() + ", [y]="+getY();
    }
}
