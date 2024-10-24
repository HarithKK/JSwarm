package examples.multiagent.leader_election.core;

import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.AbsAgent;

public class Matric {

    public static double MaxControlEnergy(Drone d, double energy){

        if(d == null)
            return -1;

        if(d.getConncetions().isEmpty()){
            return energy;
        }

        double maxEnergy = 0;
        for(AbsAgent a: d.getConncetions()){
            Drone di = (Drone)a;
            maxEnergy = Math.max(maxEnergy, MaxControlEnergy(di, energy + di.getPosition().getDistance(d.getPosition())));
        }
        return maxEnergy;
    }

    public static <List> double MaxComEnergy(RealMatrix A, List nodesList, Drone d, double energy){

        if(d == null)
            return -1;

        if(d.getConncetions().isEmpty()){
            return energy;
        }

        double maxEnergy = 0;
        for(AbsAgent a: d.getConncetions()){
            Drone di = (Drone)a;
            maxEnergy = Math.max(maxEnergy, MaxControlEnergy(di, energy + di.getPosition().getDistance(d.getPosition())));
        }
        return maxEnergy;
    }
}
