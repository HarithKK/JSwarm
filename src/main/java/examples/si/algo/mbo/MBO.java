package examples.si.algo.mbo;

import org.apache.commons.math3.analysis.function.Abs;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.si.Agent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
H. A. Abbass, "MBO: marriage in honey bees optimization-a Haplometrosis polygynous swarming approach," Proceedings of the 2001 Congress on Evolutionary Computation (IEEE Cat. No.01TH8546), 2001, pp. 207-214 vol. 1, doi: 10.1109/CEC.2001.934391.
 */

public class MBO extends SIAlgorithm {

    private static final int SPERM_COUNT = 1;
    private int numberOfWorkers, numberOfQueens,numberOfDrones, mutationCount;

    private Queen bestQueen;
    private Double alpha;

    private double minQueenSpeed, maxQueenSpeed, droneSelectionProbability, mutationProbability;

    public MBO(
            ObjectiveFunction fn,
            int numberOfWorkers,
            int numberOfDrones,
            int numberOfQueens,
            int steps,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double alpha,
            double minQueenSpeed,
            double maxQueenSpeed,
            double droneSelectionProbability,
            double mutationProbability,
            int mutationCount
    ) {
        this.objectiveFunction = fn;
        this.numberOfQueens = numberOfQueens;
        this.numberOfWorkers = numberOfWorkers;
        this.numberOfDimensions = numberOfDimensions;
        this.stepsCount = steps;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.alpha = alpha;
        this.minQueenSpeed = minQueenSpeed;
        this.maxQueenSpeed = maxQueenSpeed;
        this.droneSelectionProbability = droneSelectionProbability;
        this.mutationCount = mutationCount;
        this.mutationProbability = mutationProbability;
        this.numberOfDrones = numberOfDrones;

        try{
            addAgents("queens", Markers.CIRCLE, Color.RED);
            addAgents("workers", Markers.CIRCLE, Color.GREEN);
            addAgents("drones", Markers.CIRCLE, Color.BLUE);
            addAgents("broods", Markers.DIAMOND, Color.ORANGE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void step() throws Exception{

            for (AbsAgent agent: getAgents("queens").getAgents()) {
                Queen q = (Queen) agent;
                if(q.getEnergy() == 0)
                    continue;
                // update the speed, energy and position
                q.setPosition(Randoms.getRandomVector(this.numberOfDimensions, this.minBoundary, this.maxBoundary));
                q.setSpeed(q.getSpeed() * this.alpha);
                q.setEnergy((int) (q.getEnergy() - currentStep));
                q.updateBestValue(this.objectiveFunction, this.isGlobalMinima.isSet());
                updateSpermatheca(q);
            }
            sortQueensList();

            // came to the nest generate broods
            for (AbsAgent agent: getAgents("queens").getAgents()) {
                Queen q = (Queen) agent;
                Brood b = q.generateBrood(this.objectiveFunction, this.mutationProbability, this.mutationCount, this.isGlobalMinima.isSet());
                if(b!=null){
                    getAgents("broods").addAgent(b);
                }
            }

            sortBroodList();

            while(!getAgents("broods").getAgents().isEmpty()){
                Brood b = (Brood) getAgents("broods").getAgents().get(0);
                Queen q = (Queen) getAgents("queens").getAgents().get(getAgents("queens").getAgents().size()-1);
                if(Validator.validateBestValue(b.getFitnessValue(), q.getpBest(), isGlobalMinima.isSet())){
                    q.setpBest(b.getFitnessValue());
                    sortQueensList();
                }
                getAgents("broods").getAgents().remove(0);
            }

            for (AbsAgent q: getAgents("queens").getAgents()){
                this.updateBestQueen((Queen) q);
            }
    }

    private void sortQueensList() {
        getAgents("queens").sort(new Comparator<Queen>() {
            @Override
            public int compare(Queen o1, Queen o2) {
                if(Validator.validateBestValue(o1.getpBest(), o2.getpBest(), isGlobalMinima.isSet())){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
    }

    private void sortBroodList() {
        getAgents("broods").sort(new Comparator<Brood>() {
            @Override
            public int compare(Brood o1, Brood o2) {
                if(Validator.validateBestValue(o1.getFitnessValue(), o2.getFitnessValue(), isGlobalMinima.isSet())){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
    }

    private void updateSpermatheca(Queen q) {

        Double f1 = this.objectiveFunction.setParameters(q.getPosition().getPositionIndexes()).call();

        for (AbsAgent d: getAgents("drones").getAgents()) {
            Double f2 = this.objectiveFunction.setParameters(d.getPosition().getPositionIndexes()).call();

            double p = Math.exp(-Math.abs(f1-f2)/q.getSpeed());

            if(p >= this.droneSelectionProbability){
                q.addSperm(new Sperm(SPERM_COUNT, (Drone) d));
            }
        }

    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0;i<this.numberOfWorkers; i++){
            Worker worker = new Worker(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            getAgents("workers").getAgents().add(worker);
        }

        for(int i=0;i<this.numberOfDrones; i++){
            Drone drone = new Drone(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            getAgents("drones").getAgents().add(drone);
        }

        for(int i=0;i<this.numberOfQueens; i++){
            Queen queen = new Queen(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            queen.setSpeed(Randoms.rand(this.minQueenSpeed, this.maxQueenSpeed));
            queen.setEnergy(getStepsCount());
            queen.updateBestValue(this.objectiveFunction, this.isGlobalMinima.isSet());
            getAgents("queens").getAgents().add(queen);
            this.updateBestQueen(queen);
        }
    }

    private void updateBestQueen(Queen queen) {

        if(this.bestQueen == null){
            this.bestQueen = queen;
            return;
        }
        if(Validator.validateBestValue(queen.getpBest(), this.bestQueen.getpBest(), isGlobalMinima.isSet())){
            this.bestQueen = queen;
        }
    }

    @Override
    public Double getBestDoubleValue() {
        return this.bestQueen.getpBest();
    }

    @Override
    public String getBestVariables() {
        return this.bestQueen.getBestPosition().toString();
    }

    @Override
    public Vector getGBest() { return this.bestQueen.getBestPosition(); }

    @Override
    public SIAlgorithm clone() throws CloneNotSupportedException {
        return new MBO(
                objectiveFunction,
                numberOfWorkers,
                numberOfDrones,
                numberOfQueens,
                getStepsCount(),
                numberOfDimensions,
                minBoundary,
                maxBoundary,
                isGlobalMinima.isSet(),
                alpha,
                minQueenSpeed,
                maxQueenSpeed,
                droneSelectionProbability,
                mutationProbability,
                mutationCount);
    }

}
