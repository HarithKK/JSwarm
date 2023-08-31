package org.usa.soc.goa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.singleObjective.AckleysFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.math.BigDecimal;

public class GOA extends Algorithm {

    private int populationSize;

    private GrassHopper[] grassHoppers;

    private double cmin, cmax, intensityOfAttraction, attractionLength;

    private Vector vectordiff;

    public GOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isLocalMinima,
            double cmin,
            double cmax,
            double intensityOfAttraction,
            double attractionLength
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.isLocalMinima = isLocalMinima;
        this.cmin = cmin;
        this.cmax = cmax;
        this.intensityOfAttraction = intensityOfAttraction;
        this.attractionLength = attractionLength;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.grassHoppers = new GrassHopper[populationSize];
        this.vectordiff = new Vector(numberOfDimensions);

        for(int i=0; i< numberOfDimensions;i++){
            vectordiff.setValue((maxBoundary[i]-minBoundary[i])/2, i);
        }
    }

    @Override
    public void runOptimizer(int time) throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Grass Hoppers Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try{

            for(int step = 0; step< getStepsCount(); step++){

                double c = cmax - ((step+1)*(cmax - cmin)/stepsCount);
                Vector dc = vectordiff.operate(Vector.OPERATOR.MULP, c);

                for(int i=0 ;i< populationSize; i++){
                    for(int j=i+1; j< populationSize; j++){
                        double distance = this.grassHoppers[i].getPosition().getDistance(this.grassHoppers[j].getPosition());
                        double unitMultiplier= 2 + BigDecimal.valueOf(distance).remainder(BigDecimal.valueOf(2)).doubleValue();

                        Vector unitVector = this.grassHoppers[j].getPosition()
                                .operate(Vector.OPERATOR.SUB, this.grassHoppers[i].getPosition())
                                .operate(Vector.OPERATOR.DIV, distance + 1E-14);

                        double s = calculateS(unitMultiplier);

                        Vector distanceVector = dc.operate(Vector.OPERATOR.MULP, s)
                                .operate(Vector.OPERATOR.MULP, unitVector);

                        this.grassHoppers[i].addTotalDistance(distanceVector);
                        this.grassHoppers[j].addTotalDistance(distanceVector);
                    }

                    Vector newX = grassHoppers[i].getTotalDistanceVector()
                            .operate(Vector.OPERATOR.MULP, c)
                            .operate(Vector.OPERATOR.ADD, this.gBest.getClonedVector())
                            .fixVector(minBoundary, maxBoundary);
                    double fitnessValue = objectiveFunction.setParameters(newX.getPositionIndexes()).call();
                    grassHoppers[i].setPosition(newX);
                    grassHoppers[i].setFitnessValue(fitnessValue);
                }

                for(GrassHopper g: grassHoppers)
                    updateGbest(g);

                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest.getClonedVector(), this.getBestDoubleValue(), step);
                stepCompleted(time, step);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private double calculateS(double distance) {
        return this.intensityOfAttraction * Math.exp(-(distance)/attractionLength) - (Math.exp(-distance));
    }

    @Override
    public void initialize() {

        this.setInitialized(true);

        for(int i=0;i <populationSize; i++){
            GrassHopper grassHopper = new GrassHopper(numberOfDimensions, minBoundary, maxBoundary);
            grassHopper.setFitnessValue(objectiveFunction.setParameters(grassHopper.getPosition().getPositionIndexes()).call());

            grassHoppers[i] = grassHopper;

            updateGbest(grassHopper);
        }
    }

    private void updateGbest(GrassHopper grassHopper) {
        grassHopper.getTotalDistanceVector().resetAllValues(0.0);
        double fgbest = objectiveFunction.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
        if(Validator.validateBestValue(grassHopper.getFitnessValue(), fgbest, isLocalMinima)){
            this.gBest.setVector(grassHopper.getPosition());
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.populationSize*2];
        for(int i=0; i< this.populationSize; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.grassHoppers[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
