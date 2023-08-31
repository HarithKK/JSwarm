package org.usa.soc.mfa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MFA extends Algorithm {

    private Moth[] moths;
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

        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.moths = new Moth[numberOfMoths];
        this.flames = new ArrayList<>(numberOfMoths);
    }

    @Override
    public void runOptimizer(int time) throws Exception{

        if(!this.isInitialized()){
            throw new RuntimeException("Wolfs Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< stepsCount; step++){

            if(step == 0){
                for (Moth m: moths) {
                    Flame f = new Flame(m.getPosition());
                    f.setFitnessValue(objectiveFunction.setParameters(f.getPosition().getPositionIndexes()).call());
                    this.flames.add(f);
                }
                Collections.sort(flames, new FlameComparator());
            }else{

                List<Flame> tmpFlameList = new ArrayList<>();

                for (Flame f: flames) {
                    tmpFlameList.add(f);
                }

                for (Moth m: moths) {
                    Flame f = new Flame(m.getPosition());
                    f.setFitnessValue(objectiveFunction.setParameters(f.getPosition().getPositionIndexes()).call());
                    tmpFlameList.add(f);
                }

                Collections.sort(tmpFlameList, new FlameComparator());

                for(int i=0; i< flames.size(); i++){
                    this.flames.set(i, tmpFlameList.get(i));
                }
            }
            this.gBest.setVector(flames.get(0).getPosition().getClonedVector());

            double a = -1.0 + (double)(step + 1) * (-1.0 / (double)stepsCount);
            int flameNo = (int) Math.round((numberOfMoths - 1) - (step + 1) * ((double)(numberOfMoths - 1) / (double)stepsCount));

            for (int i=0 ; i<numberOfMoths ;i++){
                Flame tmpFlame = i <= flameNo ? flames.get(i) : flames.get(flameNo);
                double distance = tmpFlame.getPosition().operate(Vector.OPERATOR.SUB, moths[i].getPosition()).getMagnitude();

                double t = (a - 1.0) * Randoms.rand(0, 1) + 1.0;
                double firstComponent = distance * Math.exp(this.b * t) * Math.cos(2 * Math.PI * t);
                moths[i].setPosition(tmpFlame.getPosition().operate(Vector.OPERATOR.ADD, firstComponent).fixVector(minBoundary, maxBoundary));
                moths[i].setFitnessValue(objectiveFunction.setParameters(moths[i].getPosition().getPositionIndexes()).call());
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(time, step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;

    }

    @Override
    public void initialize() {
        setInitialized(true);

        for(int i=0; i< numberOfMoths; i++){
            Moth moth = new Moth(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0 , 1).fixVector(minBoundary, maxBoundary));
            moth.setFitnessValue(objectiveFunction.setParameters(moth.getPosition().getPositionIndexes()).call());
            moths[i] = moth;
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
