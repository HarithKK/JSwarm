package examples.multiagent.lfclassic;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.util.Randoms;

public class Follower extends Agent {

    Vector velocity = new Vector(2);
    @Override
    public void step() {
        //updatePosition(getPosition().operate(Vector.OPERATOR.ADD, velocity));
    }
}
