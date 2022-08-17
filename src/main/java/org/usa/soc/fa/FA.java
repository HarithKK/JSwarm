package org.usa.soc.fa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class FA extends Algorithm {

    private int numberOfFlies;

    private Fly[] flies;

    private double gama, alpha, beta0;

    public FA(ObjectiveFunction<Double> objectiveFunction,
              int stepsCount,
              int numberOfDimensions,
              int numberOfFlies,
              double[] minBoundary,
              double[] maxBoundary,
              double characteristicLength,
              double alpha,
              double beta0,
              boolean isLocalMinima){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.gama = characteristicLength;
        this.alpha = alpha;
        this.beta0 = beta0;

        this.numberOfFlies = numberOfFlies;
        this.gBest = isLocalMinima ? new Vector(numberOfDimensions).setMaxVector() : new Vector(numberOfDimensions).setMinVector();
        flies = new Fly[numberOfFlies];
    }

    @Override
    public void runOptimizer(int time) {

        if(!this.isInitialized()){
            throw new RuntimeException("Nests Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< getStepsCount(); step++){

            for(int i=0; i< numberOfFlies; i++){

                Fly fi = flies[i];
                for(int j=0; j< numberOfFlies; j++){

                    if(i==j){
                        continue;
                    }

                    Fly fj = flies[j];
                    double r = fi.getPosition().getDistance(fj.getPosition());

                    double Ii = fi.calculateIntensity(this.objectiveFunction, this.gama, r);
                    double Ij = fj.calculateIntensity(this.objectiveFunction, this.gama, r);

                    if(Validator.validateBestValue(Ij, Ii, isLocalMinima)){
                        double beta = calculateAttractiveness(r);
                        double thirdPart = alpha*(Randoms.rand(0,1) - 0.5);
                        Vector secondPart = fj.getPosition()
                                .operate(Vector.OPERATOR.SUB, fi.getPosition())
                                .operate(Vector.OPERATOR.MULP, beta);
                        fi.setPosition(
                                fi.getPosition()
                                        .operate(Vector.OPERATOR.ADD, secondPart)
                                        .operate(Vector.OPERATOR.ADD, thirdPart)
                                        .fixVector(minBoundary, maxBoundary)
                        );
                    }

                }
                updateGBest(fi);
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            sleep(time);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void updateGBest(Fly fi) {
        Double fpbest = this.objectiveFunction.setParameters(fi.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(fi.getPosition());
        }
    }

    private double calculateAttractiveness(double r) {
        return beta0 * Math.exp(-(gama * Math.pow(r, 2)));
    }

    @Override
    public void initialize() {
        setInitialized(true);

        if(!Validator.validateRangeInOneAndZero(alpha)){
            throw new RuntimeException("Alpha should be between 0 and 1");
        }

        for(int i = 0; i< numberOfFlies; i++){
            Fly d = new Fly(minBoundary, maxBoundary, numberOfDimensions);
            updateGBest(d);
            this.flies[i] = d;
        }
    }

    @Override
    public double[][] getDataPoints() {

        double[][] data = new double[this.numberOfDimensions][this.numberOfFlies];
        for(int i=0; i< this.numberOfFlies; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.flies[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
