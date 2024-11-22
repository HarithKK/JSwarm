package examples.multiagent.leader_election.core.data_structures;

import examples.multiagent.leader_election.core.Drone;
import examples.multiagent.leader_election.core.ObjectiveFunctions;
import org.apache.commons.math3.linear.RealMatrix;
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

    public Tree updateLambda(double p, double totalLabmda, double totalDistance) {
        lambda = (Math.pow(this.distance, -p) * this.w ) / ((totalDistance)* totalLabmda);
        return this;
    }

    public Tree updateWeight(double totalFitnessValue) {
        w = f1 / totalFitnessValue;
        return this;
    }

    public Tree setFitnessValues(Pair<Double, Double> f) {
        f1 = f.getFirst();
        f2 = f.getSecond();
        return this;
    }

    public Tree setFitnessValues(RealMatrix gc, Drone drone) {
        setFitnessValues(ObjectiveFunctions.f(this, gc, drone.getIndex(), drone.getPosition().getClonedVector()));
        return this;
    }

    public Tree setIndex(int index){
        this.index = index;
        return this;
    }
}
