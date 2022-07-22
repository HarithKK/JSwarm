package org.usa.soc.pso;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.intefaces.IAlgorithm;
import org.usa.soc.util.Logger;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Validator;

public class PSO implements IAlgorithm {

    private int particleCount;
    private int numberOfDimensions;
    private int stepsCount;
    private Double c1, c2, w;

    private boolean isLocalMinima;

    private double[] minBoundary, maxBoundary;
    private ObjectiveFunction<Double> objectiveFunction;

    private Particle[] particles;

    private Vector gBest;

    public PSO(
            ObjectiveFunction<Double> objectiveFunction,
            int particleCount,
            int numberOfDimensions,
            int stepsCount,
            double c1,
            double c2,
            double w,
            double []minBoundary,
            double []maxBoundary,
            boolean isLocalMinima) {

        this.particleCount = particleCount;
        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.c1 = c1;
        this.c2 = c2;
        this.w = w;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.particles = new Particle[particleCount];
        this.gBest = new Vector(numberOfDimensions);
        this.isLocalMinima = isLocalMinima;
        this.getGBest().resetAllValues(isLocalMinima ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
    }

    public void runOptimizer(){

        // initialize search space parameters
        this.initialize();

        // run the steps
        double currentBestValue = objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();

        for(int step = 1; step <= this.stepsCount; step++){
            Double stepBestValue = objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();

            if(Validator.validateBestValue(stepBestValue, currentBestValue, isLocalMinima)){
                Logger.getInstance().log("GBest [" + step + "] \t" + stepBestValue  + " |=> " + this.getGBest().toString() );
                currentBestValue = stepBestValue;
            }

            // update positions
            for (Particle p: this.particles) {
                p.updatePbest(this.objectiveFunction, this.isLocalMinima);
                this.updateGBest(p.getPBest(), this.getGBest());
            }

            // update velocity factor
            for (Particle p: this.particles) {
                p.updateVelocityAndPosition(this.getGBest(), this.c1, this.c2, this.w);
            }

        }

    }

    private void initialize(){
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        // initialize particles
        for(int i=0;i<this.particleCount; i++){
            this.particles[i] = new Particle(this.minBoundary, this.maxBoundary, this.numberOfDimensions);
            this.updateGBest(this.particles[i].getPBest(), this.getGBest());
        }

    }

    private void updateGBest(Vector pBestPosition, Vector gBestPosition) {

        Double fpbest = this.objectiveFunction.setParameters(pBestPosition.getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(gBestPosition.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.getGBest().setVector(pBestPosition, this.minBoundary, this.maxBoundary);
        }
    }

    public Vector getGBest() {
        return gBest;
    }

    public Double getGBestValue() {
        return this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
    }
    public Double getGBestValue(int round) {
        Double d = this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
        return Mathamatics.round(d, round);
    }
    public Double getGBestAbsValue(int round) {
        Double d = this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
        return Mathamatics.absRound(d, round);
    }



}
