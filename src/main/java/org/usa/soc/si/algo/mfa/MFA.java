package org.usa.soc.si.algo.mfa;

import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MFA extends Algorithm {

    private List<Moth> moths;
    private List<Flame> flames;

    private int numberOfMoths;

    private double b;

    public MFA (ObjectiveFunction<Double> objectiveFunction,
                int stepsCount,
                int numberOfDimensions,
                int numberOfMoths,
                double[] minBoundary,
                double[] maxBoundary,
                double b){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfMoths = numberOfMoths;
        this.b = b;

        this.gBest = isGlobalMinima ? new Vector(numberOfDimensions).setMaxVector() : new Vector(numberOfDimensions).setMinVector();

        this.moths = new ArrayList<Moth>(numberOfMoths);
        this.flames = new ArrayList<Flame>(numberOfMoths);
    }

    @Override
    public void runOptimizer() throws Exception {

        if(!this.isInitialized()){
            throw new RuntimeException("Moths Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< stepsCount; step++){
            if(step == 0){
                for (Moth m: moths) {
                    Flame f = new Flame(m.getPosition());
                    f.setFitnessValue(m.getFitnessValue());
                    this.flames.add(f);
                }
                Collections.sort(flames, new FlameComparator());
            }else{

                for (Moth m: moths) {
                    Flame f = new Flame(m.getPosition());
                    f.setFitnessValue(m.getFitnessValue());
                    this.flames.add(f);
                }
                Collections.sort(flames, new FlameComparator());
                this.flames = this.flames.subList(0, numberOfMoths);
            }

            this.gBest.setVector(flames.get(0).getPosition().getClonedVector());

            double a = -1.0 + (double)(step + 1) * (-1.0 / (double)stepsCount);
            int flameNo = (int) Math.round((numberOfMoths - 1) - (step + 1) * ((double)(numberOfMoths - 1) / (double)stepsCount));

            for (int i=0 ; i<numberOfMoths ;i++){
                Flame tmpFlame = i <= flameNo ? flames.get(i) : flames.get(flameNo);
                double distance = tmpFlame.getPosition().operate(Vector.OPERATOR.SUB, moths.get(i).getPosition()).getMagnitude();

                double t = (a - 1.0) * Randoms.rand(0, 1) + 1.0;
                double firstComponent = distance * Math.exp(this.b * t) * Math.cos(2 * Math.PI * t);

                Vector position = tmpFlame.getPosition().operate(Vector.OPERATOR.ADD, firstComponent).fixVector(minBoundary, maxBoundary);
                moths.get(i).setPosition(position);
                moths.get(i).setFitnessValue(objectiveFunction.setParameters(position.getPositionIndexes()).call());
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;

    }

    @Override
    public void initialize() {
        setInitialized(true);

        for(int i=0; i< numberOfMoths; i++){
            Moth moth = new Moth(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0 , 1).fixVector(minBoundary, maxBoundary));
            moth.setFitnessValue(objectiveFunction.setParameters(moth.getPosition().getPositionIndexes()).call());
            moths.add(moth);
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfMoths];
        for(int i=0; i< this.numberOfMoths; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.flames.get(i).getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
