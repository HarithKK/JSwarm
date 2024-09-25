package examples.si.algo.tsoa;

import examples.si.benchmarks.singleObjective.LevyFunction;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.awt.*;
import java.util.ArrayList;

public class TSOA extends SIAlgorithm {

    private int populationSize;

    private int seedsCount = 2;
    private double fertileHalf = 0.5;
    private double p = -1;

    private double distanceDecrement;
    private int deligator, totalSeedsCount;

    private double l = 1.0, c1, c2;

    public Vector z;

    public int totalSproutedSeedsCount;

    private double[] minDispressalRadius, maxDispressalRadius;

    public ArrayList<Double> z_history = new ArrayList<>();

    public TSOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            int seedsCount,
            double fertileHalf,
            double c1,
            double c2
    ){

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.seedsCount = seedsCount;
        this.fertileHalf = fertileHalf;
        this.c1 = c1;
        this.c2 = c2;

        this.minDispressalRadius = new double[numberOfDimensions];
        this.maxDispressalRadius = new double[numberOfDimensions];

        for(int i=0; i< numberOfDimensions; i++){
            double v = (maxBoundary[i] - minBoundary[i])/populationSize;
            minDispressalRadius[i] = -v;
            maxDispressalRadius[i] = v;
        }

        try{
            addAgents("trees", Markers.CIRCLE, Color.GREEN);
            addAgents("zTree", Markers.CIRCLE, Color.RED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void step() throws Exception {

            z = new Vector(this.numberOfDimensions);

            double totalLabmda = 0;
            double totalFitnessValue = 0.0;
            double totalDistanceWithP = 0.0;
            double minimumDistance = Double.MAX_VALUE;
            int lastCount = getAgents("trees").getAgents().size();

            for(int i =0; i < lastCount; i++){
                Tree t = (Tree) getAgents("trees").getAgents().get(i);
                totalLabmda += t.getLambda();
                totalDistanceWithP += Math.pow(t.getCalculatedDistance(z), -p);
                totalFitnessValue += t.getFitnessValue();
            }

            int zSize = (int) Math.round(populationSize - (currentStep + 1) * ((double)(populationSize - 1) / (double)stepsCount));

            for(int i=0; i<zSize ; i++){
                Tree t = ((Tree)getAgents("trees").getAgents().get(i));
                z = z.operate(Vector.OPERATOR.ADD, t.getPosition().operate(Vector.OPERATOR.MULP, t.getLambda()));
                t.updateWeight(totalFitnessValue);
                t.updateLambda(p, totalLabmda, totalDistanceWithP);
            }
            for(int i =0; i < lastCount; i++){
                Tree t = (Tree) getAgents("trees").getAgents().get(i);
                minimumDistance = Math.min(t.getCalculatedDistance(z), minimumDistance);
            }
            getAgents("zTree").getAgents().get(0).setPosition(z);
            z_history.add(objectiveFunction.setParameters(z.getPositionIndexes()).call());
            for(int i = 0; i < deligator; i++){
                Tree t = (Tree) getAgents("trees").getAgents().get(i);
                for(int j =0; j< seedsCount; j++){
                    Tree newTree = new Tree(numberOfDimensions, minBoundary, maxBoundary);

                    double r = Randoms.rand(0,1);
                    if(r < 0.0){
                        newTree.setPosition(t.getPosition().operate(Vector.OPERATOR.ADD,
                                Randoms.getRandomVector(numberOfDimensions, minDispressalRadius, maxDispressalRadius, 0,1)
                        ).fixVector(minBoundary, maxBoundary));
                        if(newTree.getPosition().getDistance(z) <= minimumDistance){
                            newTree.setFitnessValue(objectiveFunction.setParameters(newTree.getPosition().getPositionIndexes()).call());
                            newTree.setLambda(this.l);
                            getAgents("trees").getAgents().add(newTree);
                        }
                    }else if(r < 0.5){
                        Vector v1 = z.getClonedVector().operate(Vector.OPERATOR.SUB, t.getPosition())
                                .operate(Vector.OPERATOR.MULP, c1)
                                .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                        Vector v2 = this.gBest.getClonedVector()
                                .operate(Vector.OPERATOR.SUB,  t.getPosition())
                                .operate(Vector.OPERATOR.MULP, c2)
                                .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                        Vector vx = v1.operate(Vector.OPERATOR.ADD, v2);
                        newTree.setPosition(
                                t.getPosition()
                                        .operate(Vector.OPERATOR.ADD, vx)
                                        .fixVector(minBoundary, maxBoundary)
                        );
                        if(newTree.getPosition().getDistance(z) <= minimumDistance){
                            newTree.setFitnessValue(objectiveFunction.setParameters(newTree.getPosition().getPositionIndexes()).call());
                            newTree.setLambda(this.l);
                            getAgents("trees").getAgents().add(newTree);
                        }
                    }else{
                        newTree.setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1));
                        newTree.setFitnessValue(objectiveFunction.setParameters(newTree.getPosition().getPositionIndexes()).call());
                        newTree.setLambda(this.l);
                        getAgents("trees").getAgents().add(newTree);
                    }
                }
            }

            sort();
            updateGBest((Tree) getAgents("trees").getAgents().get(0));

            // Remove old trees
            totalSproutedSeedsCount = getAgents("trees").getAgents().size() - lastCount;
            for(int i = 0; i < totalSproutedSeedsCount; i++){
                getAgents("trees").getAgents().remove(getAgents("trees").getAgents().size()-1);
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

        deligator = (int)(getAgents("trees").getAgents().size() * fertileHalf);
    }

    private void updateGBest(Tree tree) {
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(tree.getPosition().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())) {
            this.gBest.setVector(tree.getPosition());
        }
    }
}
