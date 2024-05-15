package examples.multiagent;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.util.Randoms;

import java.awt.*;

class TestAgent extends Agent{

    @Override
    public void step() {
        this.updatePosition(Randoms.rand(2, 10), Randoms.rand(2, 10));
    }
}

public class InitialTest {
    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm(1000) {
            @Override
            public void initialize() {
                this.addAgents("a", new TestAgent(), 10);
                this.addAgents("b", new TestAgent(), 100, Markers.CROSS, Color.RED);
            }

            @Override
            public void run() {

            }
        };

        Executor.executePlain2D("Initial Execution",algorithm, 700, 700, new Margins(0, 15, 0, 15));
    }
}
