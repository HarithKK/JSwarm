package examples.multiagent.initial;

import org.usa.soc.multiagent.Agent;
import org.usa.soc.util.Randoms;

public class InitialTestAgent extends Agent {
    @Override
    public void step() {

        this.updatePosition(Randoms.getRandomVector(2, -20, 20)
        );
    }
}
