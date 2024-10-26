package examples.multiagent.leader_election.testcases;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.Critarian;
import examples.multiagent.leader_election.core.Drone;
import examples.multiagent.leader_election.core.Matric;
import examples.multiagent.leader_election.core.WalkType;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.Button;
import org.usa.soc.multiagent.view.TextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestCase1 {
    public static void main(String[] args) {
        Main m = new Main(10, 3, 10, 100, 100, 80, 25, 0.01, 0.001, 5, WalkType.RANDOM_THETA);

        Executor.getInstance().registerTextBox(new TextField("Max Energy"));
        Executor.getInstance().registerTextBox(new TextField("Agents"));
        Executor.getInstance().registerTextButton(new Button("Remove Leader 0").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.removeAgent(m.algorithm.getFirstAgents().stream().filter(d->((Drone)d).rank ==0).findFirst().get().getIndex());
            }
        }));

        Executor.getInstance().registerTextButton(new Button("LE").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents());
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));

        Executor.getInstance().executePlain2D("LF", m.algorithm, 700, 700, new Margins(0, 200, 0, 200));


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);

                        if(m.algorithm.isInitialized()){
                            Executor.getInstance().updateData("Agents", String.valueOf(m.algorithm.getFirstAgents().size()));
                            Executor.getInstance().updateData("Max Energy", String.valueOf(Matric.MaxControlEnergy(m.utmostLeader, 0)));
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
