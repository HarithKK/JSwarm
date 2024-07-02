package org.usa.soc.multiagent.runners;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.view.ChartView;
import progs.Main;

public class Executor {

    private static Executor instance;
    private ChartView chartView;

    private static Algorithm iAlgorithm;

    public static Executor getInstance(){
        if(instance == null){
            instance = new Executor();
        }
        return  instance;
    }

    private Executor(){}

    public static Algorithm getAlgorithm() {
        return iAlgorithm;
    }

    public void executePlain2D(String title, Algorithm algorithm, int w, int h, Margins m){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                iAlgorithm = algorithm;
                chartView = new ChartView(title, algorithm, w, h, m);
                chartView.setInterval(50);
            }
        });
    }

}