package org.usa.soc.pso;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class PSO extends Algorithm implements Cloneable {

    private int particleCount;

    private final Double c1;
    private final Double c2;
    private final Double wMax;
    private final Double wMin;

    private final Particle[] particles;

    public PSO(
            ObjectiveFunction<Double> objectiveFunction,
            int particleCount,
            int numberOfDimensions,
            int stepsCount,
            double c1,
            double c2,
            double w,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isLocalMinima) {

        this.particleCount = particleCount;
        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.c1 = c1;
        this.c2 = c2;
        this.wMax = w;
        this.wMin = w;

        this.particles = new Particle[particleCount];
        this.gBest = new Vector(numberOfDimensions);
        this.isLocalMinima = isLocalMinima;
        this.getGBest().resetAllValues(isLocalMinima ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
    }

    public PSO(
            ObjectiveFunction<Double> objectiveFunction,
            int particleCount,
            int numberOfDimensions,
            int stepsCount,
            double c1,
            double c2,
            double wMax,
            double wMin,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isLocalMinima) {

        this.particleCount = particleCount;
        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.c1 = c1;
        this.c2 = c2;
        this.wMax = wMax;
        this.wMin = wMin;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.particles = new Particle[particleCount];
        this.gBest = new Vector(numberOfDimensions);
        this.isLocalMinima = isLocalMinima;
        this.getGBest().resetAllValues(isLocalMinima ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
    }

    @Override
    public void runOptimizer(int time) {

        if (!this.isInitialized()) {
            throw new RuntimeException("Particles Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();
        // run the steps
        double currentBestValue = objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();

        for (int step = 1; step <= this.getStepsCount(); step++) {
            Double stepBestValue = objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();

            if (Validator.validateBestValue(stepBestValue, currentBestValue, isLocalMinima)) {
                currentBestValue = stepBestValue;
            }

            // update positions
            for (Particle p : this.particles) {
                p.updatePbest(this.objectiveFunction, this.isLocalMinima);
                this.updateGBest(p.getPBest(), this.getGBest());
            }

            // update velocity factor
            for (Particle p : this.particles) {
                p.updateVelocityAndPosition(this.getGBest(), this.c1, this.c2, this.calculateW(wMax, wMin, step));
            }
            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(time, step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Double calculateW(Double wMax, Double wMin, int step) {
        if (wMax == wMin)
            return wMin;
        else {
            return wMax - step * ((wMax - wMin) / this.getStepsCount());
        }
    }

    @Override
    public void initialize() {

        this.setInitialized(true);

        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);
        Validator.checkMinMax(wMax, wMin);

        // initialize particles
        for (int i = 0; i < this.particleCount; i++) {
            Particle p = new Particle(this.minBoundary, this.maxBoundary, this.numberOfDimensions);
            Vector vc = getRandomPosition(Randoms.getRandomVector(numberOfDimensions, this.minBoundary, this.maxBoundary));
            p.setPosition(vc);
            p.setPBest(vc);
            this.particles[i] = p;
            this.updateGBest(this.particles[i].getPBest(), this.getGBest());
        }
    }

    private Vector getRandomPosition(Vector v) {

        Double[] center = Mathamatics.getCenterPoint(this.numberOfDimensions, this.minBoundary, this.maxBoundary);

        if (objectiveFunction.setParameters(v.getPositionIndexes()).validateRange()) {
            return v;
        }

        boolean[] isPlus = new boolean[numberOfDimensions];
        for (int i = 0; i < numberOfDimensions; i++) {
            isPlus[i] = v.getValue(i) < center[i];
        }

        while (!isTerminated(isPlus, v.getPositionIndexes(), center, numberOfDimensions)) {

            for (int i = 0; i < numberOfDimensions; i++) {
                Double dt = v.getValue(i) + (isPlus[i] ? 0.1 : -0.1);
                v.setValue(dt, i);
                if (objectiveFunction.setParameters(v.getPositionIndexes()).validateRange()) {
                    return v;
                }
            }
        }

        return v;
    }

    private boolean isTerminated(boolean[] isPlus, Double[] positionIndexes, Double[] center, int D) {
        boolean shouldRun = true;
        for (int i = 0; i < D; i++) {
            if (isPlus[i]) {
                shouldRun = shouldRun && positionIndexes[i] < center[i];
            } else {
                shouldRun = shouldRun && positionIndexes[i] > center[i];
            }
        }
        return !shouldRun;
    }

    private void updateGBest(Vector pBestPosition, Vector gBestPosition) {

        ObjectiveFunction tfn = this.objectiveFunction.setParameters(pBestPosition.getPositionIndexes());
        Double fpbest = tfn.call();
        Double fgbest = this.objectiveFunction.setParameters(gBestPosition.getPositionIndexes()).call();

        if (Validator.validateBestValue(fpbest, fgbest, isLocalMinima)) {
            this.gBest.setVector(getRandomPosition(pBestPosition.getClonedVector()), minBoundary, maxBoundary);
        }
    }

    @Override
    public Algorithm clone() throws CloneNotSupportedException {
        return new PSO(objectiveFunction, particleCount,
                numberOfDimensions,
                getStepsCount(),
                c1,
                c2,
                wMax,
                wMin,
                minBoundary,
                maxBoundary,
                isLocalMinima);
    }

    @Override
    public double[][] getDataPoints(){
        double[][] data = new double[this.numberOfDimensions][this.particleCount];
        for(int i=0; i< this.particleCount; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.particles[i].getPosition().getValue(j),2);
            }
        }
        return data;
    };
}
