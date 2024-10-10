package examples.si.algo.ba;

import org.usa.soc.si.Agent;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Bat extends Agent {

    private Vector best;
    private Vector velocity;
    private Vector f;

    private double alpha;
    private double a;
    private double gamma;
    private double r;
    private double r0;

    private int step;

    public Bat(double[] minBoundary, double[] maxBoundary, int numberOfDimensions, double alpha, double a0, double gamma, double r0) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.alpha = alpha;
        this.a = a0;
        this.gamma = gamma;
        this.r = r0;
        this.r0 = r0;
        this.step =0;

        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
        this.velocity = new Vector(numberOfDimensions).resetAllValues(0.0);
        this.best = position.getClonedVector();
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void updatePosition(Vector gBest) {

        this.getVelocity().setVector(this.position.operate(Vector.OPERATOR.SUB, gBest)
                .operate(Vector.OPERATOR.MULP, f)
                .operate(Vector.OPERATOR.ADD, this.getVelocity()));
        this.position.setVector(this.position.operate(Vector.OPERATOR.ADD, this.getVelocity()), minBoundary, maxBoundary);
    }

    public Vector getBest() {
        return best.getClonedVector();
    }

    public Vector getVelocity() {
        return velocity;
    }
}
