package examples.multiagent.leader_election.core.data_structures;

public class VoteRPCResponse {
    public int currentTerm;
    public boolean isVoted;
    public double Q;

    public VoteRPCResponse(int currentTerm, boolean isVoted, double distance) {
        this.currentTerm = currentTerm;
        this.isVoted = isVoted;
        this.Q = distance;
    }
}
