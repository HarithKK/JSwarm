package examples.si.algo.mfa;

import org.usa.soc.si.Agent;

import java.util.Comparator;

public class FlameComparator implements Comparator<Agent> {
    @Override
    public int compare(Agent o1, Agent o2) {
        if(o1.getFitnessValue() < o2.getFitnessValue())
            return 1;
        else if (o1.getFitnessValue() == o2.getFitnessValue()) {
            return 0;
        }
        return -1;
    }
}

