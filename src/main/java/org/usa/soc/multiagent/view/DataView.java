package org.usa.soc.multiagent.view;

import org.knowm.xchart.XYSeries;
import org.usa.soc.multiagent.runners.Executor;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class DataView extends JFrame{

    JPanel panel;

    int s =0;

    public DataView(String title){
        setTitle(title);
        this.setSize(700, 1000);
        initialize();
    }

    public void addData(String chartName, String seriesName, double value){
        if(!Executor.getInstance().getChartHashMap().containsKey(chartName))
            return;
        ProgressiveChart chart = Executor.getInstance().getChartHashMap().get(chartName);
        chart.addData(seriesName, value);

        panel.updateUI();
        this.revalidate();
        this.repaint();
    }

    private void initialize() {
        panel = new JPanel();

        for(ProgressiveChart chart : Executor.getInstance().getChartHashMap().values()){
            panel.add(chart.getPanel());
        }

        this.add(panel);
    }

    public void setVisible(int loc){
        this.setLocation(loc + 20, 0);
        this.setVisible(true);
    }
}
