package org.usa.soc;

import net.ericaro.surfaceplotter.Mapper;
import org.usa.soc.benchmarks.singleObjective.StyblinskiTangFunction;
import org.usa.soc.surfacePlotter.FunctionToMapper;
import org.usa.soc.surfacePlotter.Plot;

import java.util.concurrent.Callable;

public abstract class ObjectiveFunction<T> implements Callable<Double> {
    T[] parameters;

    public ObjectiveFunction<T> setParameters (T []value) {
        this.parameters = value;
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
}