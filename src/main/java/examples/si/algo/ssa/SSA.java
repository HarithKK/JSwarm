package examples.si.algo.ssa;

import examples.si.algo.tsoa.Tree;
import org.usa.soc.core.action.Method;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class SSA extends SIAlgorithm {

    private int populationSize;

    private double Pdp, Gc, airDensity, speed, surfaceAreaBody, lossInHeight;

    private int hSquirrel, aSquirrel, nSquirrel;
    private double beta = 1.5;

    public SSA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double Pdp,
            double Gc,
            double airDensity,
            double speed,
            double surfaceAreaBody,
            double lossInHeight
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima.setValue(isGlobalMinima);

        this.Pdp = Pdp;
        this.Gc = Gc;
        this.airDensity = airDensity;
        this.speed = speed;
        this.surfaceAreaBody = surfaceAreaBody;
        this.lossInHeight = lossInHeight;
        setFirstAgents("Squirrel", new ArrayList<>(populationSize));
    }

    @Override
    public void step() throws Exception{

                Squirrel squirrelOnHickoryTree = (Squirrel) getFirstAgents().get(hSquirrel);
                Vector dg = new Vector(numberOfDimensions);
                dg.setValues(new Method() {
                    @Override
                    public double execute() {
                        return calculateRandomGlidingDistance();
                    }
                });
                // case for squirrels from acorn tree to hickory tree
                for(int i=1; i < aSquirrel;i++){
                    Squirrel squirrelOnAcornTree = (Squirrel)getFirstAgents().get(i);
                    if(Randoms.rand(0,1) > Pdp){
                        Vector vx = squirrelOnHickoryTree.getPosition()
                                .operate(Vector.OPERATOR.SUB, squirrelOnAcornTree.getPosition())
                                .operate(Vector.OPERATOR.MULP, Gc)
                                .operate(Vector.OPERATOR.MULP, dg);

                        squirrelOnAcornTree.getPosition().updateVector(vx);
                        squirrelOnAcornTree.getPosition().fixVector(minBoundary,maxBoundary);
                    }else{
                        squirrelOnAcornTree.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary));
                    }
                    squirrelOnAcornTree.setFitnessValue(objectiveFunction.setParameters(squirrelOnAcornTree.getPosition().getPositionIndexes()).call());
                }

                // normal trees and moving towards acorn trees
                for(int i = aSquirrel; i < nSquirrel; i++){
                    Squirrel squirrelOnNormalTree = (Squirrel)getFirstAgents().get(i);
                    if(Randoms.rand(0,1) > Pdp){
                        Squirrel aSquirrelObj = (Squirrel)getFirstAgents().get(Randoms.rand(1,aSquirrel));
                        Vector vx = aSquirrelObj.getPosition()
                                .operate(Vector.OPERATOR.SUB, squirrelOnNormalTree.getPosition())
                                .operate(Vector.OPERATOR.MULP, Gc)
                                .operate(Vector.OPERATOR.MULP, dg);

                        squirrelOnNormalTree.getPosition().updateVector(vx);
                        squirrelOnNormalTree.getPosition().fixVector(minBoundary,maxBoundary);
                    }else{
                        squirrelOnNormalTree.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary));
                    }
                    squirrelOnNormalTree.setFitnessValue(objectiveFunction.setParameters(squirrelOnNormalTree.getPosition().getPositionIndexes()).call());
                }

                // normal trees and moving towards hickory nut tree
                for(int i = nSquirrel; i < populationSize; i++){
                    Squirrel squirrelOnNormalTree = (Squirrel)getFirstAgents().get(i);
                    if(Randoms.rand(0,1) > Pdp){
                        Vector vx = squirrelOnHickoryTree.getPosition()
                                .operate(Vector.OPERATOR.SUB, squirrelOnNormalTree.getPosition())
                                .operate(Vector.OPERATOR.MULP, Gc)
                                .operate(Vector.OPERATOR.MULP, dg);

                        squirrelOnNormalTree.getPosition().updateVector(vx);
                        squirrelOnNormalTree.getPosition().fixVector(minBoundary,maxBoundary);
                    }else{
                        squirrelOnNormalTree.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary));
                    }
                    squirrelOnNormalTree.setFitnessValue(objectiveFunction.setParameters(squirrelOnNormalTree.getPosition().getPositionIndexes()).call());
                }

                double sc = calculateSeasonalConstant();
                double smin = 0.00001 / Mathamatics.pow(365, ((currentStep+1) * 2.5 / stepsCount));

//                if(sc < smin){
//                    for(int i=nSquirrel; i < populationSize;i++){
//                        Squirrel sq = (Squirrel)getFirstAgents().get(i);
//                        sq.setPosition(Commons.levyFlightVector(numberOfDimensions,1.5));
//                        sq.setFitnessValue(objectiveFunction.setParameters(sq.getPosition().getPositionIndexes()).call());
//                    }
//                }

                sort();
                updateGBest((Squirrel)getFirstAgents().get(0));

    }

    private void updateGBest(Squirrel sq) {
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(sq.getPosition().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())) {
            this.gBest.setVector(sq.getPosition());
        }
    }

    private double calculateSeasonalConstant() {
        double sumSt =0;

        for(int i = 1; i< aSquirrel; i++){
            sumSt += Math.pow((((Squirrel)getFirstAgents().get(i)).getFitnessValue() - ((Squirrel)getFirstAgents().get(0)).getFitnessValue()),2);
        }
        return Math.sqrt(sumSt);
    }

    private double calculateRandomGlidingDistance() {
        double Cl = Randoms.rand(0.675, 1.5);
        double L = 0.5 * Cl * speed * speed * surfaceAreaBody;
        double D = 0.5 * 0.6 * speed * speed * surfaceAreaBody;
        double tanTheta = D / L;
        return this.lossInHeight / tanTheta;
    }

    @Override
    public void initialize() {

        if(populationSize < 5){
            throw new RuntimeException("Need sufficient squirrels (>5)");
        }

        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0;i <populationSize; i++){
            Squirrel squirrel = new Squirrel(numberOfDimensions, minBoundary, maxBoundary);
            squirrel.setFitnessValue(objectiveFunction.setParameters(squirrel.getPosition().getPositionIndexes()).call());
            getFirstAgents().add(squirrel);
        }

        sort();
        updateGBest((Squirrel)getFirstAgents().get(0));

        hSquirrel = 1;
        aSquirrel = 4;
        nSquirrel = 20;

    }

}
