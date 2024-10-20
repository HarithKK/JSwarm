package org.usa.soc.multiagent.runners;

import org.usa.soc.core.Flag;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.view.Button;
import org.usa.soc.multiagent.view.ChartView;
import org.usa.soc.multiagent.view.DataBox;
import org.usa.soc.multiagent.view.DataView;
import org.usa.soc.multiagent.view.ProgressiveChart;
import org.usa.soc.multiagent.view.TextField;

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

    private HashMap<String, DataBox> chartHashMap = new HashMap<>();

    private static Algorithm iAlgorithm;

    private Flag isLegendVisible = new Flag();

    public void setLegendVisible(boolean b){
        isLegendVisible.setValue(b);
    }

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

    public void registerTextBox(TextField txt){
        chartHashMap.put(txt.getKey(), txt);
    }

    public void registerTextButton(Button btn){
        chartHashMap.put(btn.getKey(), btn);
    }

    public void updateData(String chartName, String seriesName, double value){
        if(!chartHashMap.containsKey(chartName))
            return;
        dataView.addData(chartName, seriesName, value);
    }

    public void updateData(String textFieldName, String value){
        if(!chartHashMap.containsKey(textFieldName))
            return;
        dataView.addData(textFieldName, value);
    }

    public void executePlain2D(String title, Algorithm algorithm, int w, int h, Margins m){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                iAlgorithm = algorithm;
                chartView = new ChartView(title, algorithm, w, h, m);
                chartView.getView2D().getChart().getStyler().setLegendVisible(isLegendVisible.isSet());
                getChartView().setCustomActions(customActions);
                getChartView().setInterval(150);

                if(!getDataMap().isEmpty()){
                    dataView = new DataView("Data View");
                    dataView.setVisible(w);
                }
            }
        });
    }

    public DataView getDataView() {
        return dataView;
    }

    public HashMap<String, DataBox> getDataMap() {
        return chartHashMap;
    }

    public ChartView getChartView() {
        return chartView;
    }
}
