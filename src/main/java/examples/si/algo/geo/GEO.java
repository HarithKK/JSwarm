package examples.si.algo.geo;

import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class GEO extends Algorithm {

    private int numberOfEagles;

    private Eagle[] eagles;

    private double pa0, paT, pc0, pcT, pa, pc;

    public GEO(
            ObjectiveFunction objectiveFunction,
            int numberOfEagles,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double pa0,
            double paT,
            double pc0,
            double pcT
    ) {
        this.objectiveFunction = objectiveFunction;
        this.numberOfEagles = numberOfEagles;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima = isGlobalMinima;
        this.pa0 = pa0;
        this.paT = paT;
        this.pc0 = pc0;
        this.pcT = pcT;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.eagles = new Eagle[numberOfEagles];
    }

    @Override
    public void runOptimizer() throws Exception{
        if (!this.isInitialized()) {
            throw new RuntimeException("Eagles Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();

        for (int step = 0; step < stepsCount; step++) {

            // update Pa and Pc
            pa = pa0 + (step/ stepsCount)*(paT - pa0);
            pc = pc0 - (step/ stepsCount)*(pcT - pc0);

            for(Eagle eagle: eagles){
                // Random prey location
                Eagle prey = eagles[Randoms.rand(numberOfEagles)];

                Vector attackVector = prey.getLocalBestPositon().operate(Vector.OPERATOR.SUB, eagle.getPosition());

                if(attackVector.getMagnitude() != 0){
                    double d = eagle.getPosition().operate(Vector.OPERATOR.MULP,attackVector).getNonDistanceMagnitude();
                    Vector c = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);

                    int r = Randoms.rand(numberOfDimensions);
                    double ck = (d - (attackVector.getNonDistanceMagnitude() - attackVector.getValue(r)))/attackVector.getValue(r);
                    c.setValue(ck, r);

                    Vector p1 = Randoms.getRandomVector(numberOfDimensions,  0,1)
                            .operate(Vector.OPERATOR.MULP, pa)
                            .operate(Vector.OPERATOR.MULP, attackVector.operate(Vector.OPERATOR.DIV, attackVector.getMagnitude()));
                    Vector p2 = Randoms.getRandomVector(numberOfDimensions,  0,1)
                            .operate(Vector.OPERATOR.MULP, pc)
                            .operate(Vector.OPERATOR.MULP, c.operate(Vector.OPERATOR.DIV, c.getMagnitude()));

                    Vector newX =  eagle.getPosition().operate(Vector.OPERATOR.ADD, p1.operate(Vector.OPERATOR.ADD, p2));
                    eagle.setPosition(newX.fixVector(minBoundary, maxBoundary));
                    eagle.setFitnessValue(objectiveFunction.setParameters(newX.getPositionIndexes()).call());
                    eagle.updateLocalBest(isGlobalMinima);
                }
            }

            for(Eagle eagle: eagles){
                double globalBestFitnessValue = objectiveFunction.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
                if(Validator.validateBestValue(eagle.getLocalBestFitnessValue(), globalBestFitnessValue, isGlobalMinima)){
                    this.gBest.setVector(eagle.getLocalBestPositon());
                }
            }

            if (this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for (int i=0; i< numberOfEagles; i++){
            Eagle eagle = new Eagle(numberOfDimensions, minBoundary, maxBoundary);
            eagle.setFitnessValue(objectiveFunction.setParameters(eagle.getPosition().getPositionIndexes()).call());
            eagle.setLocalBestFitnessValue(objectiveFunction.setParameters(eagle.getLocalBestPositon().getPositionIndexes()).call());
            eagles[i] = eagle;
        }

        for(Eagle eagle: eagles){
            double globalBestFitnessValue = objectiveFunction.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
            if(Validator.validateBestValue(eagle.getLocalBestFitnessValue(), globalBestFitnessValue, isGlobalMinima)){
                this.gBest.setVector(eagle.getLocalBestPositon());
            }
        }

    }



    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfEagles*2];
        for(int i=0; i< this.numberOfEagles; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.eagles[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
