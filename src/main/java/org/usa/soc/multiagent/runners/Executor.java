package org.usa.soc.multiagent.runners;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.view.ChartView;
import org.usa.soc.multiagent.view.DataView;
import org.usa.soc.multiagent.view.ProgressiveChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Executor {

    private static Executor instance;
    private ChartView chartView;
    private DataView dataView;

    private HashMap<String, ProgressiveChart> chartHashMap = new HashMap<>();

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

    List<Component> customActions = new ArrayList<>();

    public void AddCustomActions(String title, ActionListener listener, boolean enabled){
        JButton btn = new JButton(title);
        btn.addActionListener(listener);
        btn.setEnabled(enabled);
        customActions.add(btn);
    }

    public void AddCustomCheckBox(String title, ActionListener listener, boolean enabled){
        JCheckBox chk = new JCheckBox();
        JLabel label = new JLabel(title);
        chk.addActionListener(listener);
        chk.setEnabled(true);
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(chk);
        customActions.add(panel);
    }

    public void registerChart(ProgressiveChart chart){
        chartHashMap.put(chart.getChart().getTitle(), chart);
    }

    public void updateData(String chartName, String seriesName, double value){
        if(!chartHashMap.containsKey(chartName))
            return;
        dataView.addData(chartName, seriesName, value);
    }

    public void executePlain2D(String title, Algorithm algorithm, int w, int h, Margins m){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                iAlgorithm = algorithm;
                chartView = new ChartView(title, algorithm, w, h, m);
                chartView.setCustomActions(customActions);
                chartView.setInterval(50);

                if(!getChartHashMap().isEmpty()){
                    dataView = new DataView("Data View");
                    dataView.setVisible(w);
                }
            }
        });
    }

    public DataView getDataView() {
        return dataView;
    }

    public HashMap<String, ProgressiveChart> getChartHashMap() {
        return chartHashMap;
    }

}
