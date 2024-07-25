package org.usa.soc.si;

import net.ericaro.surfaceplotter.Mapper;
import org.usa.soc.si.view.plotter.FunctionToMapper;
import org.usa.soc.si.view.plotter.Plot;

import java.util.concurrent.Callable;

public abstract class ObjectiveFunction<T> implements Callable<Double> {
    T[] parameters;

    private double orderOfConvergence;

    public ObjectiveFunction<T> setParameters (T []value) {
        this.parameters = value;
        this.orderOfConvergence = 1;
        return this;
    }

    public T[] getParameters() {
        return parameters;
    }

    public abstract Double call ();
    public abstract int getNumberOfDimensions();

    public abstract double[] getMin();

    public abstract double[] getMax();

    public abstract double getExpectedBestValue();

    public abstract double[] getExpectedParameters();

    public boolean validateRange(){
        return true;
    };

    public Plot plot(){
        Mapper m = new FunctionToMapper(this);
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