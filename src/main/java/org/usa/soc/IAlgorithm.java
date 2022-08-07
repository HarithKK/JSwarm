package org.usa.soc;

import org.usa.soc.core.Vector;

public interface IAlgorithm {
    void runOptimizer();

    long getNanoDuration();

    void initialize();

    String getBestValue();

    Double getBestDValue();

    Vector getBestVector();

    ObjectiveFunction getFunction();

    String getBestVariables();

    IAlgorithm clone() throws CloneNotSupportedException;

    boolean isMinima();
}

