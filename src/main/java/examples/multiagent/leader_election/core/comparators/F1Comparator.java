package examples.multiagent.leader_election.core.comparators;

import examples.multiagent.leader_election.core.data_structures.Tree;

import java.util.Comparator;

public class F1Comparator implements Comparator<Tree> {
    @Override
    public int compare(Tree o1, Tree o2) {
        return Double.compare(o1.f1, o2.f1);
    }
}
