package examples.multiagent.leader_election.testcases;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.Critarian;
import examples.multiagent.leader_election.core.Drone;
import examples.multiagent.leader_election.core.Matric;
import examples.multiagent.leader_election.core.data_structures.WalkType;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.action.AfterEach;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.Button;
import org.usa.soc.multiagent.view.ChartSeries;
import org.usa.soc.multiagent.view.ProgressiveChart;
import org.usa.soc.multiagent.view.TextField;
import org.usa.soc.si.runners.FunctionsFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TestCase1 {

    final static double safeRange = 25.0;

    public static void main(String[] args) {
        Main m = new Main(15, 5, 3, safeRange, 0.001, 0.0001, WalkType.FORWARD);

        Executor.getInstance().registerTextBox(new TextField("Max Energy"));
        Executor.getInstance().registerTextBox(new TextField("Agents"));
        Executor.getInstance().registerTextBox(new TextField("Leader ID"));
        Executor.getInstance().registerTextBox(new TextField("Re Lambda"));
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
                int index = new Critarian().TSOA(m.model, m.getLayer(1)).index;
                m.performLE(index);
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));
//        Executor.getInstance().registerTextButton(new Button("LE ALSO").addAction(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Executor.getInstance().getChartView().getView2D().pauseExecution();
//                AbsAgent agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.ALSO);
//                m.performLE(agent.getIndex());
//                Executor.getInstance().getChartView().getView2D().resumeExecution();
//            }
//        }));
//        Executor.getInstance().registerTextButton(new Button("LE CSO").addAction(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Executor.getInstance().getChartView().getView2D().pauseExecution();
//                AbsAgent agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.CSO);
//                m.performLE(agent.getIndex());
//                Executor.getInstance().getChartView().getView2D().resumeExecution();
//            }
//        }));
//        Executor.getInstance().registerTextButton(new Button("LE PSO").addAction(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Executor.getInstance().getChartView().getView2D().pauseExecution();
//                AbsAgent agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.PSO);
//                m.performLE(agent.getIndex());
//                Executor.getInstance().getChartView().getView2D().resumeExecution();
//            }
//        }));
//        Executor.getInstance().registerTextButton(new Button("LE MFA").addAction(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Executor.getInstance().getChartView().getView2D().pauseExecution();
//                AbsAgent agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.MFA);
//                m.performLE(agent.getIndex());
//                Executor.getInstance().getChartView().getView2D().resumeExecution();
//            }
//        }));

        Executor.getInstance().registerTextButton(new Button("LE Random").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                int id = new Critarian().random(0, m.getLayer(1).size());
                m.performLE(id);
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));

        Executor.getInstance().registerTextButton(new Button("LE Raft").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().Raft(m.model,m.getLayer(1), m.partialLinks);
                System.out.println(agent);
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));

        Executor.getInstance().registerTextButton(new Button("LE GHS-JPL").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().GHS(m.model,m.getLayer(1));
                System.out.println(agent);
                m.performLE(agent.getIndex());
                Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));

        Executor.getInstance().registerTextButton(new Button("LE RUNALL").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Executor.getInstance().getChartView().getView2D().pauseExecution();
                AbsAgent agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.TSOA);
                agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.MFA);
                agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.CSO);
                agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.ALSO);
                agent = new Critarian().SI(m.model, m.getLayer(1), Critarian.SICritatianType.PSO);
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
                org.usa.soc.si.runners.Main.executeGUI(new FunctionsFactory().register(objectiveFunction).build());
                //Executor.getInstance().getChartView().getView2D().resumeExecution();
            }
        }));

        Executor.getInstance().executePlain2D("LF", m.algorithm, 700, 700, new Margins(0, 200, 0, 600), true, new AfterEach() {
            @Override
            public void execute(long step) {
                return;
            }
        });

        List<ChartSeries> ch = new ArrayList<>();
        for(int i=0; i<m.agentsCount; i++){
            ch.add(new ChartSeries(String.valueOf(i), 0));
        }

        Executor.getInstance().registerChart(
                new ProgressiveChart(400, 300, "nodel_energy", "er", "steps")
                .setMaxLength(50).subscribe(ch).setTitle(true).setLegend(true));

//        Executor.getInstance().registerChart(
//                new ProgressiveChart(200, 80, "formation_error", "e", "steps")
//                        .setLegend(false)
//                        .setMaxLength(200)
//                        .subscribe(new ChartSeries("err", 0))
//        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(100);
                        if(m.algorithm.isInitialized()){

//                            List<AbsAgent> agents = m.algorithm.getFirstAgents();
//                            for(AbsAgent a: agents){
//                                ((Drone)a).updateEnergyProfile();
//                            }

                            Executor.getInstance().updateData("Agents", String.valueOf(m.algorithm.getFirstAgents().size()));
                            Executor.getInstance().updateData("Max Energy", String.valueOf(Matric.MaxControlEnergy(m.utmostLeader, 0)));
                            Executor.getInstance().updateData("Leader ID", String.valueOf(m.utmostLeader.getIndex()));
                            Executor.getInstance().updateData("Re Lambda", String.valueOf(Matric.eigenReLambda(m.model.A)));
                            for(Drone d: m.getLayer(1)){
                                Executor.getInstance().updateData("nodel_energy", d.getIndex()+"", d.nodalEnergy);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        //throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
