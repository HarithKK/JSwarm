package org.usa.soc;

import org.usa.soc.core.Action;
import org.usa.soc.core.Vector;

public abstract class Algorithm implements Cloneable {

    protected boolean isLocalMinima;

    protected double[] minBoundary;
    protected double[] maxBoundary;

    protected Action stepAction;

    protected ObjectiveFunction<Double> objectiveFunction;

    protected int stepsCount;

    protected int numberOfDimensions;

    protected Vector gBest;

    protected long nanoDuration;

    private boolean isInitialized = false;

    public abstract void runOptimizer();

    public abstract void initialize();

    protected boolean isInitialized(){
        return this.isInitialized;
    }

    protected void setInitialized(boolean v){
        this.isInitialized = v;
    }

    public Vector getGBest() { return this.gBest; }

    public Double getBestDoubleValue() {
        return this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
    }

    public String getBestStringValue() {
        return String.valueOf(this.getBestDoubleValue());
    }

    public long getNanoDuration() {
        return nanoDuration;
    }

    public boolean isMinima() {
        return this.isLocalMinima;
    }

    public String getBestVariables() {
        return this.getGBest().toString();
    }

    public ObjectiveFunction getFunction() {
        return this.objectiveFunction;
    }

    @Override
    public Algorithm clone() throws CloneNotSupportedException{
        return null;
    }

    public void addStepAction(Action a){
        this.stepAction =a;
    }
}
