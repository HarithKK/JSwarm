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
        try{

                double c = cmax - ((currentStep+1)*(cmax - cmin)/stepsCount);
                Vector dc = vectordiff.operate(Vector.OPERATOR.MULP, c);

                for(int i=0 ;i< populationSize; i++){
                    GrassHopper gh = (GrassHopper)getFirstAgents().get(i);
                    Vector total = new Vector(numberOfDimensions);
                    total.resetAllValues(0.0);

                    for(int j=i+1; j< populationSize; j++){
                        double distance = gh.getPosition().getDistance(getFirstAgents().get(j).getPosition());
                        double unitMultiplier= 2 + BigDecimal.valueOf(distance).remainder(BigDecimal.valueOf(2)).doubleValue();

                        Vector unitVector = getFirstAgents().get(j).getPosition().getClonedVector()
                                .operate(Vector.OPERATOR.SUB, gh.getPosition().getClonedVector())
                                .operate(Vector.OPERATOR.DIV, distance + 1E-14);

                        double s = calculateS(unitMultiplier);

                        Vector distanceVector = unitVector.operate(Vector.OPERATOR.MULP, s)
                                .operate(Vector.OPERATOR.MULP, dc);

//                        ((GrassHopper)getFirstAgents().get(i)).addTotalDistance(distanceVector);
//                        ((GrassHopper)getFirstAgents().get(j)).addTotalDistance(distanceVector);
                        total.updateVector(distanceVector);
                    }

                    Vector newX = total.getClonedVector()
                            .operate(Vector.OPERATOR.MULP, c)
                            .operate(Vector.OPERATOR.ADD, this.gBest.getClonedVector())
                            .fixVector(minBoundary, maxBoundary);
                    gh.setPosition(newX);
                    gh.calcFitnessValue(objectiveFunction);
                    updateGbest(gh);
                }

                for(AbsAgent g: getFirstAgents())
                    updateGbest((GrassHopper) g);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private double calculateS(double distance) {
        return this.intensityOfAttraction * Math.exp(-(distance)/attractionLength) - (Math.exp(-distance));
    }

    @Override
    public void initialize() {

        this.setInitialized(true);

        for(int i=0;i <populationSize; i++){
            GrassHopper grassHopper = new GrassHopper(numberOfDimensions, minBoundary, maxBoundary);
            grassHopper.calcFitnessValue(objectiveFunction);
            getFirstAgents().add(grassHopper);
            updateGbest(grassHopper);
        }
    }

    private void updateGbest(GrassHopper grassHopper) {
        if(Validator.validateBestValue(grassHopper.getFitnessValue(), getBestDoubleValue(), isGlobalMinima.isSet())){
            this.gBest.setVector(grassHopper.getPosition());
            this.updateBestValue();
        }
    }
}
