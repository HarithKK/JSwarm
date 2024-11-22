package examples.multiagent.leader_election.core.data_structures;

public class Node {

    public int index;

    public int level = 0;
    boolean inMST = false;

    public double J;

    public Node(int index, double v){
        this.index = index;
        this.J = v;
    }

    @Override
    public String toString() {
        return String.valueOf(index);
    }
}
