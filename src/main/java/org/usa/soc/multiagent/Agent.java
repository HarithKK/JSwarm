package org.usa.soc.multiagent;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

import java.util.List;

public abstract class Agent implements Cloneable{

    private Vector position;

    private double[] xm, ym;

    public void initPosition(Margins m, List<Agent> list){
        this.xm = new double[]{m.xMin, m.xMax};
        this.ym = new double[]{m.yMin, m.yMax};
        this.setPosition(Randoms.getRandomVector(2, xm, ym));
    }

    public void updatePosition(double x, double y){
        this.setX(x);
        this.setY(y);
    }

    public void updatePosition(Vector v){
        this.position.setVector(v.getClonedVector());
    }

    public abstract void step();

    public void checkMargins() {
        this.getPosition().setVector(this.getPosition().fixVector(this.xm, this.ym));
    }

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

    public double getXMin(){ return this.xm[0]; }
    public double getXMax(){ return this.xm[1]; }
    public double getYMin(){ return this.ym[0]; }
    public double getYMax(){ return this.ym[1]; }

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
}
