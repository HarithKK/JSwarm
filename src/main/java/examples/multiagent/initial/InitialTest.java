package examples.multiagent.initial;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;

public class InitialTest {
    public static void main(String[] args) {

        Algorithm algorithm = new Algorithm(5) {
            @Override
            public void initialize() {
                try {
                    this.addAgents("agents", InitialTestAgent.class, 10);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void step() {

            }
        };

        Executor.getInstance().executePlain2D("T1",algorithm, 700, 700, new Margins(-50, 50, -50, 50));
    }
}
