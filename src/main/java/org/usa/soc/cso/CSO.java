package org.usa.soc.cso;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.pso.Particle;
import org.usa.soc.util.Validator;

public class CSO extends Algorithm {

    private Cat[] cats;
    private int numberOfCats;

    private double seekersToTracersRatio;

    private int smp;
    private double cdc, srd, c;
    private boolean spc;

    public CSO(
            ObjectiveFunction<Double> objectiveFunction,
            int numberOfDimensions,
            int stepsCount,
            int numberOfCats,
            double seekersToTracersRatio,
            double[] minBoundary,
            double[] maxBoundary,
            int smp,
            double cdc,
            double srd,
            boolean spc,
            double c,
            boolean isLocalMinima) {

        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.gBest = isLocalMinima ? new Vector(numberOfDimensions).setMaxVector() : new Vector(numberOfDimensions).setMinVector();
        this.isLocalMinima = isLocalMinima;
        this.numberOfCats = numberOfCats;
        this.seekersToTracersRatio = seekersToTracersRatio;
        this.smp = smp;
        this.cdc = cdc;
        this.srd = srd;
        this.spc = spc;
        this.c = c;
        this.cats = new Cat[numberOfCats];
    }

    @Override
    public void runOptimizer() {

        if(!this.isInitialized()){
            throw new RuntimeException("Cats Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        for(int i=0; i< this.stepsCount; i++){

            for (Cat cat: this.cats) {
                if(cat.isSeeker()){
                    cat.seek(objectiveFunction, isMinima());
                }else{
                    cat.trace(c);
                }
                cat.updateMode();
                updateBestCat(cat);
            }
            this.stepAction.performAction(this.gBest);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public void initialize() {

        this.setInitialized(true);

        if(!Validator.validateRangeInOneAndZero(this.seekersToTracersRatio)){
            throw new RuntimeException("Seekers to Tracers should be in between 0 and 1");
        }

        if(!Validator.validateRangeInOneAndZero(this.cdc)){
            throw new RuntimeException("CDC should be in between 0 and 1");
        }

        int seekersCount = (int)(this.seekersToTracersRatio * this.numberOfCats);

        int i = 0;
        while(i< seekersCount){
            this.cats[i] = new Cat(i+1,this.minBoundary, this.maxBoundary, this.numberOfDimensions, Mode.SEEKER, this.smp, this.cdc, this.srd, this.spc);
            this.updateBestCat(this.cats[i]);
            i++;
        }
        while(i< this.numberOfCats){
            this.cats[i] = new Cat(i+1,this.minBoundary, this.maxBoundary, this.numberOfDimensions, Mode.TRACER, this.smp, this.cdc, this.srd, this.spc);
            this.updateBestCat(this.cats[i]);
            i++;
        }
    }

    private void updateBestCat(Cat cat) {

        Double fpbest = this.objectiveFunction.setParameters(cat.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(cat.getPosition().getClonedVector(), this.minBoundary, this.maxBoundary);
        }
    }
}
