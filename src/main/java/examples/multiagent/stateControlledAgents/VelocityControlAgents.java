package examples.multiagent.stateControlledAgents;

import examples.multiagent.lfclassic.Follower;
import examples.multiagent.lfclassic.Leader;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.VelocityControlledAgent;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.util.Randoms;

import java.awt.*;

public class VelocityControlAgents {

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm() {

            AgentGroup ag;
            double angle = 90;

            @Override
            public void initialize() {
                try {
                    ag = this.addAgents("agents", VelocityControlledAgent.class, 5);
                    for(Agent a: ag.getAgents()){
                        ((VelocityControlledAgent)a).setAngles(0, 0.1, Math.toRadians(angle));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void run() {
                if(angle > 360)
                    angle = 0;
                else{
                    angle += 1;
                }
                for(Agent a : ag.getAgents()){
                    ((VelocityControlledAgent)a).setAngles(0, 0.2, Math.toRadians(angle));
                }
            }
        };

        Executor.getInstance().executePlain2D("Velocity Controlled Agent",algorithm, 700, 700, new Margins(-500, 500, -500, 500));
    }
}
