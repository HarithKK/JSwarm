package examples.multiagent.stateControlledAgents;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.VelocityControlledAgent;
import org.usa.soc.multiagent.runners.Executor;

public class VelocityControlAgents {

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm() {

            AgentGroup ag;
            double angle = 90;

            @Override
            public void initialize() {
                try {
                    ag = this.addAgents("agents", VelocityControlledAgent.class, 5);
                    for(AbsAgent a: ag.getAgents()){
                        ((VelocityControlledAgent)a).setAngles(0, 0.1, Math.toRadians(angle));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void step() throws Exception {

            }

            @Override
            public void run() {
                if(angle > 360)
                    angle = 0;
                else{
                    angle += 1;
                }
                for(AbsAgent a : ag.getAgents()){
                    ((VelocityControlledAgent)a).setAngles(0, 0.2, Math.toRadians(angle));
                }
            }
        };

        Executor.getInstance().executePlain2D("Velocity Controlled Agent",algorithm, 700, 700, new Margins(-500, 500, -500, 500));
    }
}
