package org.usa.soc.multiagent.runners;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.view.ChartView;
import progs.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

    List<JButton> customActions = new ArrayList<>();

    public void AddCustomActions(String title, ActionListener listener, boolean enabled){
        JButton btn = new JButton(title);
        btn.addActionListener(listener);
        btn.setEnabled(enabled);
        customActions.add(btn);
    }

    public void executePlain2D(String title, Algorithm algorithm, int w, int h, Margins m){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                iAlgorithm = algorithm;
                chartView = new ChartView(title, algorithm, w, h, m);
                chartView.setCustomActions(customActions);
                chartView.setInterval(50);
            }
        });
    }

}
