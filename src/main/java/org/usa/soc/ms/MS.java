package org.usa.soc.ms;

import org.usa.soc.Algorithm;
import org.usa.soc.IAlgorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.aco.Ant;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class MS extends Algorithm {

    private final Monky[] monkeys;
    private final int numberOfMonkeys;
    private final int maxHeightOfTheTree;

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
        this.objectiveFunction = fn;
        this.stepsCount = numberOfIterations;
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
        if (!this.isInitialized()) {
            throw new RuntimeException("Ants Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();

        for (int i = 0; i < this.stepsCount; i++) {
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
        this.setInitialized(true);

        for (int i = 0; i < this.numberOfMonkeys; i++) {
            Monky m = new Monky(
                    this.maxBoundary,
                    this.minBoundary,
                    this.numberOfDimensions,
                    this.maxHeightOfTheTree,
                    this.objectiveFunction
            );
            this.monkeys[i] = m;
            this.updateGBest(m);
        }
    }

    private void updateGBest(Monky m) {

        Double fpbest = this.objectiveFunction.setParameters(m.getBestRoot().getClonedVector().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isLocalMinima)) {
            this.gBest.setVector(m.getBestRoot(), this.minBoundary, this.maxBoundary);
        }
    }

    @Override
    public Algorithm clone() throws CloneNotSupportedException {
        return new MS(objectiveFunction,
                stepsCount,
                numberOfMonkeys,
                numberOfDimensions,
                maxHeightOfTheTree,
                minBoundary,
                maxBoundary,
                c1,
                isLocalMinima
        );
    }
}
