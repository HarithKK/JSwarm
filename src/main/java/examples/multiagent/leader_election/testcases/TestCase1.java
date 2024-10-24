package examples.multiagent.leader_election.testcases;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.Utils;
import examples.multiagent.leader_election.core.WalkType;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.Table;
import org.usa.soc.multiagent.view.TextField;

public class TestCase1 {
    public static void main(String[] args) {
        Main m = new Main(4, 8, 30, 100, 100, 80, 25, 0.01, 0.001, 5, WalkType.CIRCLE);

        Executor.getInstance().registerTextBox(new TextField("Max Energy"));
        Executor.getInstance().executePlain2D("LF", m.algorithm, 700, 700, new Margins(0, 200, 0, 200));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);

                        if(m.algorithm.isInitialized()){
                            Executor.getInstance().updateData("Max Energy", String.valueOf(Utils.finaMaxControlEnergy(m.utmostLeader, 0)));
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
