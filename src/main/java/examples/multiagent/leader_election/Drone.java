package examples.multiagent.leader_election;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;

public class Drone extends Agent {
    int rank = -1;

    Vector velocity = new Vector(2);

    @Override
    public void step() {
        this.getPosition().updateVector(velocity);
    }
}
