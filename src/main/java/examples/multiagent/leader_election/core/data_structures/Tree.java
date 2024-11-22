package examples.multiagent.leader_election.core.data_structures;

import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.ds.Vector;

public class Tree {
    public int index =0;
    public Vector position;
    public double f1 =0, f2 =0, w=0, lambda =1.0, distance =0, closenessCentrality;
    public Tree(Vector pos){
        this.position =pos;
    }

    public double getCalculatedDistance(Vector predicted) {
        this.distance = this.position.operate(Vector.OPERATOR.SUB, predicted).getMagnitude();
        return this.distance;
    }

    public void updateLambda(double p, double totalLabmda, double totalDistance) {
        lambda = (Math.pow(this.distance, -p) * this.w ) / ((totalDistance)* totalLabmda);
    }

    public void updateWeight(double totalFitnessValue) {
        w = f1 / totalFitnessValue;
    }

    public void setFitnessValues(Pair<Double, Double> f) {
        f1 = f.getFirst();
        f2 = f.getSecond();
    }
}
