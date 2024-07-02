package examples.multiagent.lfclassic;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.util.Randoms;

import java.awt.*;

public class LFClassicDynamics {

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm(5) {

            Leader al;
            AgentGroup group;
            @Override
            public void initialize() {
                try {
                    this.addAgents("leader", Leader.class, 1);
                    al = (Leader) this.getAgents().get("leader").getAgents().get(0);
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
                for(int i=0; i< group.getAgentsCount(); i++){
                    Vector v = new Vector(2);
                    Vector v2 = al.getPosition().operate(Vector.OPERATOR.SUB, group.getAgents().get(i).getPosition());
                    for(int j=0; j< group.getAgentsCount(); j++){
                        if(i!=j){
                            Vector vx = group.getAgents().get(i).getPosition().operate(
                                    Vector.OPERATOR.SUB, group.getAgents().get(j).getPosition());
                            v.setVector(v.operate(Vector.OPERATOR.ADD, vx));
                        }
                    }

                    ((Follower)group.getAgents().get(i)).velocity.setVector(v.operate(Vector.OPERATOR.ADD, v2).operate(Vector.OPERATOR.MULP, -1.0));

                }
            }
        };

        Executor.getInstance().executePlain2D("LF Execution",algorithm, 700, 700, new Margins(0, 1000, 0, 1000));
    }
}
