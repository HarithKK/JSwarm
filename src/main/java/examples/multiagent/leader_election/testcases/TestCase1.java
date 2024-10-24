package examples.multiagent.leader_election.testcases;

import examples.multiagent.leader_election.Main;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.runners.Executor;

public class TestCase1 {
    public static void main(String[] args) {
        Main m = new Main(5,15);

        Executor.getInstance().executePlain2D("LF", m.algorithm, 700, 700, new Margins(0, 200, 0, 200));
    }
}
