package examples.si.algo.tsoa;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.awt.*;

public class TSOA extends SIAlgorithm {

    private int populationSize;

    private double distanceFactor =0;

    private int seedsCount = 2;
    private double delegatorSplit = 0.5;
    private double p = -1;

    private double distanceDecrement;
    private int deligator, totalSeedsCount;

    private double l = 1.0;

    public TSOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double distanceFactor,
            int seedsCount,
            double delegatorSplit
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
        this.delegatorSplit = delegatorSplit;

        try{
            addAgents("trees", Markers.CIRCLE, Color.GREEN);
            addAgents("zTree", Markers.CIRCLE, Color.RED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void step() throws Exception {

            Vector z = new Vector(this.numberOfDimensions);

            double totalLabmda = 0;
            double totalFitnessValue = 0.0;
            double totalDistance = 0.0;
            double minimumDistance = Double.MAX_VALUE;

            int zSize = (int) Math.round(populationSize - (currentStep + 1) * ((double)(populationSize - 1) / (double)stepsCount));
            for(int i=0; i<zSize ; i++){
                Tree t = ((Tree)getAgents("trees").getAgents().get(i));
                totalLabmda += t.getLambda();
                z = z.operate(Vector.OPERATOR.ADD, t.getPosition().operate(Vector.OPERATOR.MULP, t.getLambda()));
            }
            z = z.operate(Vector.OPERATOR.DIV, totalLabmda).fixVector(minBoundary, maxBoundary);

            for(AbsAgent tree: getAgents("trees").getAgents()){
                Tree t = ((Tree)tree);
                minimumDistance = Math.min(t.getCalculatedDistance(z), minimumDistance);
            }
            getAgents("zTree").getAgents().get(0).setPosition(z);

            int totalSproutedSeedsCount = 0;
            for(int i = 0; i < deligator; i++){
                Tree t = (Tree) getAgents("trees").getAgents().get(i);
                for(int j =0; j< seedsCount; j++){
                    Tree newTree = new Tree(numberOfDimensions, minBoundary, maxBoundary);
                    boolean isSeedSprouted = false;

                    if(Randoms.rand(0,1) < 0.5){
                        newTree.setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary));
                        isSeedSprouted = true;
                    }else{
                        newTree.setPosition(t.getPosition().operate(Vector.OPERATOR.ADD,
                                Randoms.getRandomVector(numberOfDimensions, -distanceFactor, +distanceFactor)).fixVector(minBoundary, maxBoundary));
                        isSeedSprouted = true;
                        if(newTree.getPosition().getDistance(z) <= minimumDistance * distanceFactor){
                            isSeedSprouted = true;
                        }
                    }

                    if(isSeedSprouted){
                        newTree.setFitnessValue(objectiveFunction.setParameters(newTree.getPosition().getPositionIndexes()).call());
                        newTree.setLambda(this.l);
                        getAgents("trees").getAgents().add(newTree);
                        totalSproutedSeedsCount ++;
                    }
                }
            }

            sort();
            updateGBest((Tree) getAgents("trees").getAgents().get(0));
            distanceFactor *= (1-distanceDecrement);

            // Remove old trees
            for(int i = 0; i < totalSproutedSeedsCount; i++){
                getAgents("trees").getAgents().remove(getAgents("trees").getAgents().size()-1);
            }

            // update lambda and weights
            totalLabmda = 0.0;


            for(AbsAgent tree: getAgents("trees").getAgents()){
                Tree t = ((Tree)tree);
                totalLabmda += t.getLambda();
                totalFitnessValue += t.getFitnessValue();
                totalDistance += t.getCalculatedDistance(z);
            }

            for(AbsAgent tree: getAgents("trees").getAgents()){
                Tree t = (Tree)tree;
                t.updateWeight(totalFitnessValue);
                t.updateLambda(p,totalLabmda,totalDistance);
            }
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);
        double totaFitnessValue = 0.0;
        for(int i=0; i<this.populationSize; i++){
            Tree tree = new Tree(numberOfDimensions, minBoundary, maxBoundary);
            tree.setFitnessValue(objectiveFunction.setParameters(tree.getPosition().getPositionIndexes()).call());
            tree.setLambda(this.l);
            totaFitnessValue += tree.getFitnessValue();
            getAgents("trees").getAgents().add(tree);
        }

        for(AbsAgent tree: getAgents("trees").getAgents()){
            Tree t = (Tree)tree;
            t.updateWeight(totaFitnessValue);
        }

        sort();
        updateGBest((Tree) getAgents("trees").getAgents().get(0));

        Tree zTree = new Tree(numberOfDimensions, minBoundary, maxBoundary);
        zTree.setPosition(getAgents("trees").getAgents().get(0).getPosition().getClonedVector());
        getAgents("zTree").getAgents().add(zTree);

        distanceDecrement = distanceFactor/stepsCount;

        deligator = (int)(getAgents("trees").getAgents().size() * delegatorSplit);
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
