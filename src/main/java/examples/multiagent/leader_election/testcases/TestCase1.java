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
import org.usa.soc.si.runners.FunctionsFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestCase1 {
    public static void main(String[] args) {
        Main m = new Main(20, 5, 20, 100, 100, 80, 25, 0.01, 0.001, 5, WalkType.RANDOM_THETA);

        Executor.getInstance().registerTextBox(new TextField("Max Energy"));
        Executor.getInstance().registerTextBox(new TextField("Agents"));
        TextField tf = new TextField("SI Index");
        tf.setData("0");

        Executor.getInstance().registerTextBox(tf);
        Executor.getInstance().registerTextButton(new Button("Remove Leader 0").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.removeAgent(m.algorithm.getFirstAgents().stream().filter(d->((Drone)d).rank ==0).findFirst().get().getIndex());
            }
        }));

        Executor.getInstance().registerTextButton(new Button("LE TSOA").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.TSOA);
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));
        Executor.getInstance().registerTextButton(new Button("LE ALSO").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.ALSO);
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));
        Executor.getInstance().registerTextButton(new Button("LE CSO").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.CSO);
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));
        Executor.getInstance().registerTextButton(new Button("LE PSO").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.PSO);
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));
        Executor.getInstance().registerTextButton(new Button("LE MFA").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.MFA);
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));

        Executor.getInstance().registerTextButton(new Button("LE Random").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                int id = new Critarian().random(0, m.getLayer(1).size());
                m.performLE(id);
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));

        Executor.getInstance().registerTextButton(new Button("LE RUNALL").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.TSOA);
                agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.MFA);
                agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.CSO);
                agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.ALSO);
                agent = new Critarian().SI(m.model, m.algorithm.getFirstAgents(), Critarian.SICritatianType.PSO);
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));


        Executor.getInstance().registerTextButton(new Button("RUN SI").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                int index = Integer.parseInt(((TextField)Executor.getInstance().getDataMap().get("SI Index")).getData());
                Drone d = (Drone)m.algorithm.getFirstAgents().get(index);
                OF objectiveFunction = new OF(m.model.calcGcStep(m.model.getNN(), 1), d.getIndex(), d.getPosition().getClonedVector().toPoint2D());
                org.usa.soc.si.runners.Main.executeMain(new FunctionsFactory().register(objectiveFunction).build());
                //Executor.getInstance().getChartView().getView2D().resumeExecution();
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
