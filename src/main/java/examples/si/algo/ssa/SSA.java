package examples.si.algo.ssa;

import org.usa.soc.core.AbsAgent;
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

    private int hSquirrel,aSquirrelLower ,aSquirrelUpper;

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
                // case for squirrels from acorn tree to hickory tree
                for(int i=aSquirrelLower; i <= aSquirrelUpper;i++){
                    Squirrel squirrelOnAcornTree = (Squirrel)getFirstAgents().get(i);
                    double dg = calculateRandomGlidingDistance();
                    if(Randoms.rand(0,1) > Pdp){
                        squirrelOnAcornTree.setPosition(
                                squirrelOnHickoryTree.getPosition()
                                        .operate(Vector.OPERATOR.SUB,squirrelOnAcornTree.getPosition().getClonedVector())
                                        .operate(Vector.OPERATOR.MULP, Gc)
                                        .operate(Vector.OPERATOR.MULP, dg)
                                        .operate(Vector.OPERATOR.ADD, squirrelOnAcornTree.getPosition().getClonedVector())
                        );
                    }else{
                        squirrelOnAcornTree.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary, 0,1));
                    }
                    squirrelOnAcornTree.setFitnessValue(objectiveFunction.setParameters(squirrelOnAcornTree.getPosition().getPositionIndexes()).call());
                }

                // Other squirrels
                for(int i=0; i<aSquirrelLower;i++){
                    Squirrel squirrel = (Squirrel)getFirstAgents().get(i);
                    Squirrel randomSquirrelOnAcornTree = (Squirrel)getFirstAgents().get(Randoms.rand(2)+1);
                    double dg = calculateRandomGlidingDistance();
                    // case for squirrels from normal tree to acorn tree
                    if(Randoms.rand(0,1) <= 0.5){
                        if(Randoms.rand(0,1) >= Pdp){
                            if(Randoms.rand(0,1) > Pdp){
                                squirrel.setPosition(
                                        squirrelOnHickoryTree.getPosition()
                                                .operate(Vector.OPERATOR.SUB,randomSquirrelOnAcornTree.getPosition().getClonedVector())
                                                .operate(Vector.OPERATOR.MULP, Gc)
                                                .operate(Vector.OPERATOR.MULP, dg)
                                                .operate(Vector.OPERATOR.ADD, randomSquirrelOnAcornTree.getPosition().getClonedVector())
                                );
                            }else{
                                squirrel.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary, 0,1));
                            }
                        }else{

                        }
                    }else{
                        // case for squirrels from normal tree to hickory tree for food supply
                        if(Randoms.rand(0,1) > Pdp){
                            squirrel.setPosition(
                                    squirrelOnHickoryTree.getPosition()
                                            .operate(Vector.OPERATOR.SUB,squirrelOnHickoryTree.getPosition().getClonedVector())
                                            .operate(Vector.OPERATOR.MULP, Gc)
                                            .operate(Vector.OPERATOR.MULP, dg)
                                            .operate(Vector.OPERATOR.ADD, squirrelOnHickoryTree.getPosition().getClonedVector())
                            );
                        }else{
                            squirrel.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary, 0,1));
                        }
                    }
                    squirrel.setFitnessValue(objectiveFunction.setParameters(squirrel.getPosition().getPositionIndexes()).call());
                }

                double sc = calculateSeasonalConstant();
                double smin = 0.00001 / Mathamatics.pow(365, ((currentStep+1) * 2.5 / stepsCount));

                if(sc < smin){
                    for (AbsAgent s: getFirstAgents()) {
                        s.setPosition(getLevyVector());
                        ((Squirrel)s).setFitnessValue(objectiveFunction.setParameters(s.getPosition().getPositionIndexes()).call());
                    }
                }

                sort();

                double fgbest = objectiveFunction.setParameters(gBest.getClonedVector().getPositionIndexes()).call();
                if(Validator.validateBestValue(((Squirrel)getFirstAgents().get(hSquirrel)).getFitnessValue(), fgbest, isGlobalMinima.isSet())){
                    gBest.setVector(getFirstAgents().get(hSquirrel).getPosition());
                }
    }

    private double calculateSeasonalConstant() {
        double sumSt =0;

        for(int i=1; i<4;i++){
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

        for(int i=0;i <populationSize; i++){
            Squirrel squirrel = new Squirrel(numberOfDimensions, minBoundary, maxBoundary);
            squirrel.setFitnessValue(objectiveFunction.setParameters(squirrel.getPosition().getPositionIndexes()).call());

            getFirstAgents().add(squirrel);
        }

        sort();

        hSquirrel = populationSize -1;
        aSquirrelLower = populationSize - 4;
        aSquirrelUpper = populationSize - 2;
    }

    private Vector getLevyVector() {
        Vector v = new Vector(this.numberOfDimensions);
        double levy = Commons.levyflight(3);
        for(int i=0;i<numberOfDimensions;i++){
            v.setValue(minBoundary[i] + levy*(maxBoundary[i] - minBoundary[i]),i);
        }
        return v;
    }
}
