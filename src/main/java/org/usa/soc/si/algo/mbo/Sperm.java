package org.usa.soc.si.algo.mbo;

public class Sperm {
    private int spermCount;
    private Drone owner;

    public Sperm(int spermCount, Drone owner) {
        this.spermCount = spermCount;
        this.owner = owner;
    }

    public Drone getOwner() {
        return owner;
    }
}
