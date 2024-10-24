package examples.multiagent.leader_election.core;

import org.usa.soc.core.AbsAgent;

public class Utils {

    public static double finaMaxControlEnergy(Drone d, double energy){

        if(d.getConncetions().isEmpty()){
            return energy;
        }

        double maxEnergy = 0;
        for(AbsAgent a: d.getConncetions()){
            Drone di = (Drone)a;
            maxEnergy = Math.max(maxEnergy, finaMaxControlEnergy(di, energy + di.getPosition().getDistance(d.getPosition())));
        }
        return maxEnergy;
    }
}
