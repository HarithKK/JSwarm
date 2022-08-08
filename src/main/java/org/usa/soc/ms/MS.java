package org.usa.soc.ms;

import org.usa.soc.IAlgorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.aco.Ant;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class MS implements IAlgorithm {

    private final ObjectiveFunction fn;
    private final Monky[] monkeys;
    private final int numberOfIterations;
    private final int numberOfMonkeys;
    private final int numberOfDimensions;

    private final int maxHeightOfTheTree;
    private final double[] minBoundary;
    private final double[] maxBoundary;
    private boolean isInitialized = false;
    private Vector gBest;
    private final boolean isLocalMinima;
    private long nanoDuration;

    private final double c1;

    public MS(
            ObjectiveFunction fn,
            int numberOfIterations,
            int numberOfMonkeys,
            int numberOfDimensions,
            int maxHeightOfTheTree,
            double[] minBoundary,
            double[] maxBoundary,
            double c1,
            boolean isLocalMinima) {
        this.fn = fn;
        this.numberOfIterations = numberOfIterations;
        this.numberOfMonkeys = numberOfMonkeys;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.maxHeightOfTheTree = maxHeightOfTheTree;
        this.c1 = c1;

        this.monkeys = new Monky[this.numberOfMonkeys];
        this.gBest = Randoms.getRandomVector(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
    }

    @Override
    public void runOptimizer() {
        if (!this.isInitialized) {
            throw new RuntimeException("Ants Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();

        for (int i = 0; i < this.numberOfIterations; i++) {
            for (Monky m : this.monkeys) {
                m.climbTree(this.c1,this.isLocalMinima);
                updateGBest(m);
            }
        }

        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public long getNanoDuration() {
        return this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.isInitialized = true;

        for (int i = 0; i < this.numberOfMonkeys; i++) {
            Monky m = new Monky(
                    this.maxBoundary,
                    this.minBoundary,
                    this.numberOfDimensions,
                    this.maxHeightOfTheTree,
                    this.fn
            );
            this.monkeys[i] = m;
            this.updateGBest(m);
        }
    }

    private void updateGBest(Monky m) {

        Double fpbest = this.fn.setParameters(m.getBestRoot().getClonedVector().getPositionIndexes()).call();
        Double fgbest = this.fn.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isLocalMinima)) {
            this.gBest.setVector(m.getBestRoot(), this.minBoundary, this.maxBoundary);
        }
    }

    @Override
    public String getBestValue() {
        return String.valueOf(this.fn.setParameters(this.gBest.getPositionIndexes()).call());
    }

    @Override
    public Double getBestDValue() {
        return this.fn.setParameters(this.gBest.getPositionIndexes()).call();
    }

    @Override
    public Vector getBestVector() {
        return this.gBest;
    }

    @Override
    public ObjectiveFunction getFunction() {
        return this.fn;
    }

    @Override
    public String getBestVariables() {
        return this.gBest.toString();
    }

    @Override
    public IAlgorithm clone() throws CloneNotSupportedException {
        return new MS(fn,
                numberOfIterations,
                numberOfMonkeys,
                numberOfDimensions,
                maxHeightOfTheTree,
                minBoundary,
                maxBoundary,
                c1,
                isLocalMinima
        );
    }

    @Override
    public boolean isMinima() {
        return this.isLocalMinima;
    }
}
