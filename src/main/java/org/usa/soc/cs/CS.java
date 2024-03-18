package org.usa.soc.cs;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class CS extends Algorithm {

    private Nest[] nests;

    private int numberOfNests;

    private double alpha, pa;

    public CS(
            ObjectiveFunction<Double> objectiveFunction,
            int stepsCount,
            int numberOfDimensions,
            int numberOfNests,
            double[] minBoundary,
            double[] maxBoundary,
            double alpha,
            double pa,
            boolean isGlobalMinima
    ){
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima = isGlobalMinima;
        this.numberOfNests = numberOfNests;
        this.alpha = alpha;
        this.pa = pa;

        nests = new Nest[numberOfNests];
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
    }

    @Override
    public void runOptimizer() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Nests Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< getStepsCount(); step++){

            for (int i=0; i< numberOfNests; i++) {
                // get cuckoo
                Vector cuckooPosition = Mathamatics.getLevyVector(step, numberOfDimensions, 1, 3)
                        .operate(Vector.OPERATOR.MULP, this.alpha)
                        .operate(Vector.OPERATOR.ADD, nests[i].getPosition())
                        .fixVector(minBoundary, maxBoundary);

                // select the nest randomly
                int j = Math.min( (int) Math.abs(Randoms.rand(0, 1) * numberOfNests + 1) -1, numberOfNests -1);

                Double fj = this.objectiveFunction.setParameters(this.nests[j].getPosition().getPositionIndexes()).call();
                Double fi = this.objectiveFunction.setParameters(this.nests[i].getPosition().getPositionIndexes()).call();

                if(Validator.validateBestValue(fi, fj, isGlobalMinima)){
                    this.nests[j].setPosition(cuckooPosition);
                }

                if (Randoms.rand(0, 1) < pa) {
                    ReInitiateWorstNest();
                }

                updateGBest(nests[i]);
            }
                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
                stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void updateGBest(Nest nest) {
        Double fpbest = this.objectiveFunction.setParameters(nest.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima)){
            this.gBest.setVector(nest.getPosition());
        }
    }

    private void ReInitiateWorstNest() {
        //find worst
        int i0 = 0;
        for(int i=1; i< numberOfNests; i++){
            Double fi = this.objectiveFunction.setParameters(nests[i0].getPosition().getPositionIndexes()).call();
            Double fj = this.objectiveFunction.setParameters(nests[i].getPosition().getPositionIndexes()).call();

            if(Validator.validateBestValue(fi, fj, isGlobalMinima)){
                i0 = i;
            }
        }
        nests[i0].setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1));
    }

    @Override
    public void initialize() {
        this.setInitialized(true);

        if(!Validator.validateRangeInOneAndZero(pa)){
            throw new RuntimeException("Pa should be between 0 and 1");
        }

        for(int i=0; i< this.numberOfNests; i++){
            nests[i] = new Nest(minBoundary, maxBoundary, numberOfDimensions);
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfNests];
        for(int i=0; i< this.numberOfNests; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.nests[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
