package org.usa.soc.tco;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Validator;

public class TCO extends Algorithm {

    private int numberOfTermites;

    private Termite[] termites;

    private double p0, eRate, r, omega;

    public TCO(ObjectiveFunction<Double> objectiveFunction,
               int stepsCount,
               int numberOfDimensions,
               int numberOfTermites,
               double[] minBoundary,
               double[] maxBoundary,
               double p0,
               double evaporationRate,
               double r,
               double omega,
               boolean isLocalMinima){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.numberOfTermites = numberOfTermites;
        this.p0 = p0;
        this.eRate = evaporationRate;
        this.r = r;
        this.omega = omega;

        this.gBest = isLocalMinima ? new Vector(numberOfDimensions).setMaxVector() : new Vector(numberOfDimensions).setMinVector();

        this.termites = new Termite[numberOfTermites];
    }

    @Override
    public void runOptimizer(int time) {
        if(!this.isInitialized()){
            throw new RuntimeException("Termites Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< getStepsCount(); step++){

            for (Termite t: termites) {
                t.updatePheramoneValue(eRate, objectiveFunction);
            }

            for(int i=0 ;i< numberOfTermites; i++){
                Termite ti = termites[i];
                Termite tb = getClosestTermite(ti.getPosition(), i);

                if(tb != null){
                    ti.updatePositionByRandomWalk(r);
                }else{
                    if(ti.getpValue() < tb.getpValue()){
                        ti.updatePosition(omega, gBest);
                    }
                }

                updateGBest(ti);
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(time);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void updateGBest(Termite ti) {
        Double fpbest = this.objectiveFunction.setParameters(ti.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(ti.getPosition());
        }
    }

    private Termite getClosestTermite(Vector position, int c) {
        double diff = Double.MAX_VALUE;
        Termite t = null;
        for(int i=0 ;i< numberOfTermites; i++){
            Termite tj = termites[i];
            double d = position.getDistance(tj.getPosition());
            if(d <= r && d < diff){
                diff = d;
                t = tj;
            }
        }
        return t;
    }

    @Override
    public void initialize() {
        setInitialized(true);

        for(int i=0 ;i< numberOfTermites; i++){
            this.termites[i] = new Termite(minBoundary, maxBoundary, numberOfDimensions, p0);
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfTermites];
        for(int i=0; i< this.numberOfTermites; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.termites[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
