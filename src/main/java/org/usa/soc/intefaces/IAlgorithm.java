package org.usa.soc.intefaces;

import org.usa.soc.ObjectiveFunction;

public interface IAlgorithm {
    void runOptimizer();

    long getNanoDuration();

    void initialize();

    void setBoundaries(double []minBoundary, double []maxBoundary);

    String getBestValue();

    ObjectiveFunction getFunction();

    String getBestVariables();
}
