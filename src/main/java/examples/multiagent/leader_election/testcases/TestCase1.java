package examples.multiagent.leader_election.testcases;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.WalkType;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.Table;

public class TestCase1 {
    public static void main(String[] args) {
        Main m = new Main(4, 8, 30, 100, 100, 80, 25, 0.01, 0.001, 5, WalkType.STILL);

        Executor.getInstance().executePlain2D("LF", m.algorithm, 700, 700, new Margins(0, 200, 0, 200));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(50);

                        if(m.algorithm.isInitialized()){
                            Executor.getInstance().updateData("GA", m.model.GA);
                            Executor.getInstance().updateData("GB", m.model.GB);
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
