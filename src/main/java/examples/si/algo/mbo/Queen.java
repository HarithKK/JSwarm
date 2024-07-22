package examples.si.algo.mbo;

import org.usa.soc.si.Agent;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.GeneticFunctions;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Agent {

    private Vector bestPosition;
    private double speed;

    private int energy;

    private double pBest;

    private List<Sperm> spermatheca;

    public Queen(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary));
        this.maxBoundary = maxBoundary;
        this.minBoundary = minBoundary;
        this.setpBest(Double.POSITIVE_INFINITY);
        this.spermatheca = new ArrayList<>();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void addSperm(Sperm i) {
        this.spermatheca.add(i);
    }

    public Brood generateBrood(ObjectiveFunction fn, double mp, int mc, boolean isMinima) {

        if(this.spermatheca.isEmpty()){
            return  null;
        }
        int r = this.spermatheca.size() > 0 ? Randoms.rand(this.spermatheca.size()) : 0;
        Sperm sperm = this.spermatheca.get(r);

        Double fValue = new GeneticFunctions(
                fn.setParameters(this.getPosition().getPositionIndexes()).call(),
                fn.setParameters(sperm.getOwner().getPosition().getPositionIndexes()).call())
                .crossover()
                .mutate(mp,mc).getFittestValue(isMinima);
        return new Brood(fValue);

    }
    public void updateBestValue(ObjectiveFunction fn, boolean isGlobalMinima){
        double sb = fn.setParameters(this.position.getPositionIndexes()).call();

        if(Validator.validateBestValue(sb, this.getpBest(), isGlobalMinima)){
            this.setpBest(sb);
            this.setBestPosition(this.position.getClonedVector());
        }
    }

    public double getpBest() {
        return pBest;
    }

    public void setpBest(double pBest) {
        this.pBest = pBest;
    }

    public Vector getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(Vector bestPosition) {
        this.bestPosition = bestPosition;
    }
}
