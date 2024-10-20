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
        if(!Executor.getInstance().getDataMap().containsKey(chartName))
            return;
        DataBox box = Executor.getInstance().getDataMap().get(chartName);

        if(box instanceof ProgressiveChart) {
            ProgressiveChart chart = (ProgressiveChart) box;
            chart.addData(seriesName, value);
        }

        panel.updateUI();
        this.revalidate();
        this.repaint();
    }

    public void addData(String textFieldName, String value){
        if(!Executor.getInstance().getDataMap().containsKey(textFieldName))
            return;
        DataBox box = Executor.getInstance().getDataMap().get(textFieldName);

        if(box instanceof TextField) {
            TextField chart = (TextField) box;
            chart.setData(value);
        }

        panel.updateUI();
        this.revalidate();
        this.repaint();
    }

    private void initialize() {
        panel = new JPanel();

        for(DataBox box : Executor.getInstance().getDataMap().values()){
            if(box instanceof ProgressiveChart)
                panel.add(((ProgressiveChart)box).getPanel());
            if(box instanceof TextField)
                panel.add(((TextField)box).getPanel());
            if(box instanceof Button)
                panel.add(((Button)box).getPanel());
        }

        this.add(panel);
    }

    public void setVisible(int loc){
        this.setLocation(loc + 20, 0);
        this.setVisible(true);
    }
}
