package org.usa.soc.si.algo.tco;

import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
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
               boolean isGlobalMinima){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima = isGlobalMinima;
        this.numberOfTermites = numberOfTermites;
        this.p0 = p0;
        this.eRate = evaporationRate;
        this.r = r;
        this.omega = omega;

        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.termites = new Termite[numberOfTermites];
    }

    @Override
    public void runOptimizer() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Termites Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        Vector tau = new Vector(numberOfDimensions);
        Vector tauDecrements = new Vector(numberOfDimensions);

        for(int i=0;i<numberOfDimensions;i++){
            double diff = Math.abs(objectiveFunction.getMax()[i] - objectiveFunction.getMin()[i]);
            double max = diff/2;
            double min = diff/8;

            tau.setValue(max, i);
            tauDecrements.setValue((max - min)/stepsCount, i);
        }

        for(int step = 0; step< getStepsCount(); step++){

            for (Termite t: termites) {
                t.updatePheramoneValue(eRate, objectiveFunction);
            }

            for(int i=0 ;i< numberOfTermites; i++){
                Termite ti = termites[i];
                Termite tb = getClosestTermite(i,ti.getPosition(), tau.getMagnitude());

                if(tb == null){
                    ti.updatePositionByRandomWalk(tau);
                }
                else{
                    if(ti.getpValue() < tb.getpValue()){
                        updateGBest(tb);
                        ti.updatePosition(omega, tb.getPosition());
                    }else{
                        // Our Work
                        ti.updatePositionByRandomWalk(tau);
                    }
                }
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(step);
            tau.setVector(tau.operate(Vector.OPERATOR.SUB, tauDecrements));
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void updateGBest(Termite ti) {
        Double fpbest = this.objectiveFunction.setParameters(ti.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima)){
            this.gBest.setVector(ti.getPosition());
        }
    }

    private Termite getClosestTermite(int j, Vector position, double c) {
        double diff = Double.MAX_VALUE;
        Termite t = null;
        for(int i=0 ;i< numberOfTermites; i++){
            if(i==j)
                continue;
            Termite tj = termites[i];
            double d = position.getDistance(tj.getPosition());
            if(d < c && d < diff){
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
