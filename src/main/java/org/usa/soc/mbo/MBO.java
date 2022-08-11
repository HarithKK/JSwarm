package org.usa.soc.mbo;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
H. A. Abbass, "MBO: marriage in honey bees optimization-a Haplometrosis polygynous swarming approach," Proceedings of the 2001 Congress on Evolutionary Computation (IEEE Cat. No.01TH8546), 2001, pp. 207-214 vol. 1, doi: 10.1109/CEC.2001.934391.
 */

public class MBO extends Algorithm {

    private static final int SPERM_COUNT = 1;
    private int numberOfWorkers, numberOfQueens,numberOfDrones, mutationCount;

    private List<Queen> queens;
    private Worker[] workers;
    private Drone[] drones;
    private List<Brood> broodsList;

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
            boolean isLocalMinima,
            double alpha,
            double minQueenSpeed,
            double maxQueenSpeed,
            double droneSelectionProbability,
            double mutationProbability,
            int mutationCount
    ){
        this.objectiveFunction = fn;
        this.numberOfQueens = numberOfQueens;
        this.numberOfWorkers = numberOfWorkers;
        this.numberOfDimensions = numberOfDimensions;
        this.stepsCount = steps;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.alpha = alpha;
        this.minQueenSpeed = minQueenSpeed;
        this.maxQueenSpeed = maxQueenSpeed;
        this.droneSelectionProbability = droneSelectionProbability;
        this.mutationCount = mutationCount;
        this.mutationProbability = mutationProbability;
        this.numberOfDrones = numberOfDrones;

        this.queens = new ArrayList<>(this.numberOfQueens);
        this.workers = new Worker[numberOfWorkers];
        this.drones = new Drone[numberOfDrones];
        this.broodsList = new ArrayList<>();
    }

    @Override
    public void runOptimizer() {

        if(!this.isInitialized()){
            throw new RuntimeException("Particles Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();

        for(int i=0; i< this.stepsCount; i++){
            for (Queen q: queens) {
                if(q.getEnergy() == 0)
                    continue;
                // update the speed, energy and position
                q.setPosition(Randoms.getRandomVector(this.numberOfDimensions, this.minBoundary, this.maxBoundary));
                q.setSpeed(q.getSpeed() * this.alpha);
                q.setEnergy(q.getEnergy() - i);
                q.updateBestValue(this.objectiveFunction, this.isLocalMinima);
                updateSpermatheca(q);
            }
            sortQueensList();

            // came to the nest generate broods
            for (Queen q: queens) {
                Brood b = q.generateBrood(this.objectiveFunction, this.mutationProbability, this.mutationCount, this.isLocalMinima);
                if(b!=null){
                    this.broodsList.add(b);
                }
            }

            sortBroodList();

            while(!this.broodsList.isEmpty()){
                Brood b = this.broodsList.get(0);
                Queen q = this.queens.get(this.queens.size()-1);
                if(Validator.validateBestValue(b.getFitnessValue(), q.getpBest(), isLocalMinima)){
                    q.setpBest(b.getFitnessValue());
                    sortQueensList();
                }
                this.broodsList.remove(0);
            }

            for (Queen q: queens){
                this.updateBestQueen(q);
            }
            this.stepAction.performAction(this.gBest, this.getBestDoubleValue());
        }


        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void sortQueensList() {
        Collections.sort(this.queens, new Comparator<Queen>() {
            @Override
            public int compare(Queen o1, Queen o2) {
                if(Validator.validateBestValue(o1.getpBest(), o2.getpBest(), isLocalMinima)){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
    }

    private void sortBroodList() {
        Collections.sort(this.broodsList, new Comparator<Brood>() {
            @Override
            public int compare(Brood o1, Brood o2) {
                if(Validator.validateBestValue(o1.getFitnessValue(), o2.getFitnessValue(), isLocalMinima)){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
    }

    private void updateSpermatheca(Queen q) {

        Double f1 = this.objectiveFunction.setParameters(q.getPosition().getPositionIndexes()).call();

        for (Drone d: this.drones) {
            Double f2 = this.objectiveFunction.setParameters(d.getPosition().getPositionIndexes()).call();

            double p = Math.exp(-Math.abs(f1-f2)/q.getSpeed());

            if(p >= this.droneSelectionProbability){
                q.addSperm(new Sperm(SPERM_COUNT, d));
            }
        }

    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0;i<this.numberOfWorkers; i++){
            Worker worker = new Worker(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            this.workers[i] = worker;
        }

        for(int i=0;i<this.numberOfDrones; i++){
            Drone drone = new Drone(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            this.drones[i] = drone;
        }

        for(int i=0;i<this.numberOfQueens; i++){
            Queen queen = new Queen(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            queen.setSpeed(Randoms.rand(this.minQueenSpeed, this.maxQueenSpeed));
            queen.setEnergy(stepsCount);
            queen.updateBestValue(this.objectiveFunction, this.isLocalMinima);
            this.queens.add(queen);
            this.updateBestQueen(queen);
        }
    }

    private void updateBestQueen(Queen queen) {

        if(this.bestQueen == null){
            this.bestQueen = queen;
            return;
        }
        if(Validator.validateBestValue(queen.getpBest(), this.bestQueen.getpBest(), isLocalMinima)){
            this.bestQueen = queen;
        }
    }
    @Override
    public String getBestStringValue() {
        return String.valueOf(this.getBestDoubleValue());
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
    public Algorithm clone() throws CloneNotSupportedException {
        return new MBO(
                objectiveFunction,
                numberOfWorkers,
                numberOfDrones,
                numberOfQueens,
                stepsCount,
                numberOfDimensions,
                minBoundary,
                maxBoundary,
                isLocalMinima,
                alpha,
                minQueenSpeed,
                maxQueenSpeed,
                droneSelectionProbability,
                mutationProbability,
                mutationCount);
    }
}
