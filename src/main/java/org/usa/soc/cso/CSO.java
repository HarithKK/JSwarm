package org.usa.soc.cso;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class CSO extends Algorithm {

    private Cat[] cats;
    private int numberOfCats;

    private double seekersToTracersRatio;

    private int smp;
    private double cdc, srd, c, w;
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
            double w,
            boolean isLocalMinima) {

        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.numberOfCats = numberOfCats;
        this.seekersToTracersRatio = seekersToTracersRatio;
        this.smp = smp;
        this.cdc = cdc;
        this.srd = srd;
        this.spc = spc;
        this.c = c;
        this.w =w;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.cats = new Cat[numberOfCats];
    }

    @Override
    public void runOptimizer(int time) throws Exception{

        if(!this.isInitialized()){
            throw new RuntimeException("Cats Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        for(int i = 0; i< this.getStepsCount(); i++){

            for (Cat cat: this.cats) {
                if(cat.isSeeker()){
                    cat.seek(objectiveFunction, isMinima());
                }else{
                    cat.trace(c, w, gBest);
                }
                cat.updateMode();
                updateBestCat(cat);
            }
            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), i);
            stepCompleted(time, i);
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

    @Override
    public double[][] getDataPoints(){
        double[][] data = new double[this.numberOfDimensions][this.numberOfCats];
        for(int i=0; i< this.numberOfCats; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.cats[i].getPosition().getValue(j),2);
            }
        }
        return data;
    };
}
