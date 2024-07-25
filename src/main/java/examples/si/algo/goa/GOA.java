package examples.si.algo.goa;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.math.BigDecimal;
import java.util.ArrayList;

public class GOA extends SIAlgorithm {

    private int populationSize;

    private double cmin, cmax, intensityOfAttraction, attractionLength;

    private Vector vectordiff;

    public GOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
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
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.cmin = cmin;
        this.cmax = cmax;
        this.intensityOfAttraction = intensityOfAttraction;
        this.attractionLength = attractionLength;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        setFirstAgents("Grasshoppers", new ArrayList<>(populationSize));
        this.vectordiff = new Vector(numberOfDimensions);

        for(int i=0; i< numberOfDimensions;i++){
            vectordiff.setValue((maxBoundary[i]-minBoundary[i])/2, i);
        }
    }

    @Override
    public void step() throws Exception{
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
                        double distance = getFirstAgents().get(i).getPosition().getDistance(getFirstAgents().get(j).getPosition());
                        double unitMultiplier= 2 + BigDecimal.valueOf(distance).remainder(BigDecimal.valueOf(2)).doubleValue();

                        Vector unitVector = getFirstAgents().get(j).getPosition()
                                .operate(Vector.OPERATOR.SUB, getFirstAgents().get(i).getPosition())
                                .operate(Vector.OPERATOR.DIV, distance + 1E-14);

                        double s = calculateS(unitMultiplier);

                        Vector distanceVector = dc.operate(Vector.OPERATOR.MULP, s)
                                .operate(Vector.OPERATOR.MULP, unitVector);

                        ((GrassHopper)getFirstAgents().get(i)).addTotalDistance(distanceVector);
                        ((GrassHopper)getFirstAgents().get(j)).addTotalDistance(distanceVector);
                    }

                    Vector newX = ((GrassHopper)getFirstAgents().get(i)).getTotalDistanceVector()
                            .operate(Vector.OPERATOR.MULP, c)
                            .operate(Vector.OPERATOR.ADD, this.gBest.getClonedVector())
                            .fixVector(minBoundary, maxBoundary);
                    double fitnessValue = objectiveFunction.setParameters(newX.getPositionIndexes()).call();
                    getFirstAgents().get(i).setPosition(newX);
                    ((GrassHopper)getFirstAgents().get(i)).setFitnessValue(fitnessValue);
                }

                for(AbsAgent g: getFirstAgents())
                    updateGbest((GrassHopper) g);

                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest.getClonedVector(), this.getBestDoubleValue(), step);
                stepCompleted(step);
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

            getFirstAgents().add(grassHopper);

            updateGbest(grassHopper);
        }
    }

    private void updateGbest(GrassHopper grassHopper) {
        grassHopper.getTotalDistanceVector().resetAllValues(0.0);
        double fgbest = objectiveFunction.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
        if(Validator.validateBestValue(grassHopper.getFitnessValue(), fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(grassHopper.getPosition());
        }
    }
}
