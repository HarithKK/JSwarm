package org.usa.soc.si;

import net.ericaro.surfaceplotter.Mapper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.view.plotter.FunctionToMapper;
import org.usa.soc.si.view.plotter.Plot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class ObjectiveFunction<T> implements Callable<Double> {
    T[] parameters;

    protected int numberOfDimensions = 2;

    private boolean isFixedDimentions = false;

    private double orderOfConvergence;

    public ObjectiveFunction<T> setParameters (T []value) {
        this.parameters = value;
        this.orderOfConvergence = 1;
        return this;
    }

    public ObjectiveFunction updateDimensions(int n){
        if (!isFixedDimentions)
            numberOfDimensions = n;
        return this;
    }

    public ObjectiveFunction setFixedDimentions(int n){
        numberOfDimensions = n;
        return this;
    }

    public T[] getParameters() {
        return parameters;
    }

    public Vector getParametersCommonVector() {
        return new Vector(numberOfDimensions).setValues(Arrays.asList(parameters).stream().mapToDouble(d-> (Double)d).toArray());
    }

    public RealMatrix getParametersMatrix() {
        RealMatrix rm = MatrixUtils.createRealMatrix(1, numberOfDimensions);
        for(int i=0; i<numberOfDimensions; i++){
            rm.addToEntry(0, i, ((Double)getParameters()[i]).doubleValue());
        }
        return rm;
    }

    public RealVector getParametersVector() {
        return new ArrayRealVector(Arrays.asList(getParameters()).stream().mapToDouble(d -> (Double)d).toArray());
    }

    public abstract Double call ();
    public int getNumberOfDimensions(){
        return numberOfDimensions;
    }

    public abstract double[] getMin();

    public abstract double[] getMax();

    public abstract double getExpectedBestValue();

    public abstract double[] getExpectedParameters();

    public boolean validateRange(){
        return true;
    };

    public Plot plot(){
        FunctionToMapper m = new FunctionToMapper(this);
        Plot p = new Plot(m,"Function Display", true);
        p.execute(false);
        return p;
    }

    public double getOrderOfConvergence() {
        return orderOfConvergence;
    }

    public void setOrderOfConvergence(double orderOfConvergence) {
        this.orderOfConvergence = orderOfConvergence;
    }
}