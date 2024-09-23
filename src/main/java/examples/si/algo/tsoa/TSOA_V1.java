package examples.si.algo.tsoa;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class TSOA_V1 extends SIAlgorithm {

    private int populationSize;

    private double distanceFactor =0;

    private double c1, c2;

    private int seedsCount = 2;
    private double delegator_split = 0.3;
    private double p = -1;

    private double distanceDecrement;
    private int deligator, totalSeedsCount;



    public TSOA_V1(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double distanceFactor,
            int seedsCount
    ){

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.distanceFactor = distanceFactor;
        this.seedsCount = seedsCount;

        this.c1 = 10;
        this.c2 = 10;

        setFirstAgents("trees", new ArrayList<>());
    }

    @Override
    public void step() throws Exception {
        Vector predicted = new Vector(this.numberOfDimensions);

        for(int i =0; i < getFirstAgents().size(); i++){
            Tree t = (Tree) getFirstAgents().get(i);
            predicted = predicted.operate(Vector.OPERATOR.ADD, t.getPosition().operate(Vector.OPERATOR.MULP, t.getLambda()));
        }

        double totalLabmda = 0;
        double totalDistance = 0;
        for(int i =0; i < getFirstAgents().size(); i++){
            Tree t = (Tree) getFirstAgents().get(i);
            totalLabmda += t.getLambda();
            totalDistance += Math.pow(t.getCalculatedDistance(predicted), -p);
        }

        for(int i = 0; i < deligator; i++){
            Tree t = (Tree) getFirstAgents().get(i);
            t.updateLambda(p, totalLabmda, totalDistance);
            for(int j =0; j< seedsCount; j++){
                Tree newTree = new Tree(numberOfDimensions, minBoundary, maxBoundary);
                if(Randoms.rand(0,1) < 0.5){
                    Vector v1 = predicted.operate(Vector.OPERATOR.SUB, t.getPosition())
                            .operate(Vector.OPERATOR.MULP, c1)
                            .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                    Vector v2 = this.gBest.getClonedVector()
                            .operate(Vector.OPERATOR.SUB, t.getPosition())
                            .operate(Vector.OPERATOR.MULP, c2)
                            .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                    Vector vx = v1.operate(Vector.OPERATOR.ADD, v2)
                            .operate(Vector.OPERATOR.MULP, distanceFactor);
                    newTree.setPosition(
                            t.getPosition()
                                    .operate(Vector.OPERATOR.ADD, vx)
                                    .fixVector(minBoundary, maxBoundary)
                    );
                }else{
                    newTree.setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary));
                }

                newTree.setFitnessValue(objectiveFunction.setParameters(newTree.getPosition().getPositionIndexes()).call());

                newTree.setLambda(t.getLambda());
                getFirstAgents().add(newTree);
            }

            distanceFactor *= (1-distanceDecrement);
        }

        sort();
        updateGBest((Tree) getFirstAgents().get(0));

        for(int i = 0; i < totalSeedsCount; i++){
            getFirstAgents().remove(getFirstAgents().size()-1);
        }
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0; i<this.populationSize; i++){
            Tree tree = new Tree(numberOfDimensions, minBoundary, maxBoundary);
            tree.setFitnessValue(objectiveFunction.setParameters(tree.getPosition().getPositionIndexes()).call());

            getFirstAgents().add(tree);
        }

        sort();
        updateGBest((Tree) getFirstAgents().get(0));

        distanceDecrement = distanceFactor/stepsCount;

        deligator = (int)(getFirstAgents().size() * delegator_split);
        totalSeedsCount = deligator*seedsCount;
    }

    private void updateGBest(Tree tree) {
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(tree.getPosition().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())) {
            this.gBest.setVector(tree.getPosition());
        }
    }
}
