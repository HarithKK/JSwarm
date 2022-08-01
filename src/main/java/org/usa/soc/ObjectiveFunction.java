package org.usa.soc;

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

    public boolean validateRange(){
        return true;
    };
}