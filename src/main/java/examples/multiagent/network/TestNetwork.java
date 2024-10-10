package examples.multiagent.network;

import examples.multiagent.initial.InitialTestAgent;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.util.Randoms;

import java.util.List;

public class TestNetwork {

    public static void main(String[] args) {

        Algorithm algorithm = new Algorithm(5) {
            @Override
            public void initialize() {

                try {
                    this.addAgents("agents", InitialTestAgent.class, 10);
                    List<AbsAgent> nodes = getAgents("agents").getAgents();
                    nodes.get(0).addConnection(nodes.get(1)).addConnection(nodes.get(5));
                    nodes.get(0).addConnection(nodes.get(2)).addConnection(nodes.get(4));
                    nodes.get(2).addConnection(nodes.get(3)).addConnection(nodes.get(6));
                    nodes.get(3).addConnection(nodes.get(7)).addConnection(nodes.get(9));
                    nodes.get(5).addConnection(nodes.get(3)).addConnection(nodes.get(4));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void step() {

            }
        };

        Executor.getInstance().setLegendVisible(false);
        Executor.getInstance().executePlain2D("T1",algorithm, 700, 700, new Margins(-50, 50, -50, 50));

    }
}
