package examples.multiagent.lfclassic;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.util.Randoms;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LFClassicDynamics {

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm(5) {

            Leader al;
            AgentGroup group;
            @Override
            public void initialize() {
                try {
                    this.addAgents("leader", Leader.class, 1);
                    al = (Leader) this.agents.get("leader").getAgents().get(0);
                    al.setPosition(Randoms.getRandomVector(2, new double[]{-100, 100}, new double[]{100, 100}));

                    group = this.addAgents("follower", Follower.class, 10, Markers.CIRCLE, Color.RED);

                    for(int i=0;i<group.getAgentsCount();i++){
                        group.getAgents().get(i).setX(al.getX() - Randoms.rand(5, 50));
                        group.getAgents().get(i).setY(al.getY()+ Randoms.rand(5, 50));
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void run() {
                for(Agent ai : group.getAgents()){
                    ((Follower)ai).velocity.setVector(al.getPosition().operate(Vector.OPERATOR.SUB, ai.getPosition()));

                }
            }
        };

        Executor.getInstance().executePlain2D("Initial Execution",algorithm, 700, 700, new Margins(-300, 300, -300, 300));
    }
}
