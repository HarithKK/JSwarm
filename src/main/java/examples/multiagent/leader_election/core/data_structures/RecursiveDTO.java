package examples.multiagent.leader_election.core.data_structures;

import examples.multiagent.leader_election.core.Drone;

public class RecursiveDTO {
    public Drone firstAgent;
    public Drone bestAgent;
    public Drone parentAgent;

    public RecursiveDTO setFirstAgent(Drone drone){
        this.firstAgent = drone;
        return this;
    }

    public RecursiveDTO setBestAgent(Drone drone){
        this.bestAgent = drone;
        return this;
    }

    public RecursiveDTO setParentAgent(Drone drone){
        this.parentAgent = drone;
        return this;
    }
}
