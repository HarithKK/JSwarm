package examples.multiagent.leader_election.core.data_structures;

public class TSOAResponse {
    public Tree z;
    public Tree current;

    public TSOAResponse(Tree z, Tree i){
        this.z = z;
        this.current = i;
    }
}
