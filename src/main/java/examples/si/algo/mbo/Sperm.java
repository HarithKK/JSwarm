package examples.si.algo.mbo;

import org.usa.soc.si.Agent;

public class Sperm extends Agent {
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
