package org.usa.soc.multiagent.view;

import org.apache.commons.math3.linear.RealMatrix;
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

    public void addData(String textFieldName, RealMatrix matrix){
        if(!Executor.getInstance().getDataMap().containsKey(textFieldName))
            return;
        DataBox box = Executor.getInstance().getDataMap().get(textFieldName);

        if(box instanceof Table) {
            Table chart = (Table) box;
            chart.setData(matrix);
        }

        panel.updateUI();
        this.revalidate();
        this.repaint();
    }

    private void initialize() {
        panel = new JPanel();
        JPanel btnPanel = new JPanel();
        JPanel chartPanel = new JPanel();
        JPanel tablePanel = new JPanel();
        JPanel textPanel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for(DataBox box : Executor.getInstance().getDataMap().values()){
            if(box instanceof ProgressiveChart)
                chartPanel.add(((ProgressiveChart)box).getPanel());
            if(box instanceof TextField)
                textPanel.add(((TextField)box).getPanel());
            if(box instanceof Button)
                btnPanel.add(((Button)box).getPanel());
            if(box instanceof Table)
                tablePanel.add(((Table)box).getPanel());
        }
        if(btnPanel.getComponentCount() > 0) {
            btnPanel.setLayout(new GridLayout((int)Math.ceil(btnPanel.getComponentCount()/3),3));
            panel.add(btnPanel);
        }
        if(textPanel.getComponentCount() > 0) {
            textPanel.setLayout(new GridLayout((int) Math.ceil(textPanel.getComponentCount() / 3), 3));
            panel.add(textPanel);
        }
        if(chartPanel.getComponentCount() > 0) {
            chartPanel.setLayout(new GridLayout((int) Math.ceil(chartPanel.getComponentCount() / 3), 3));
            panel.add(chartPanel);
        }
        if(tablePanel.getComponentCount() > 0) {
            tablePanel.setLayout(new BorderLayout());
            panel.add(tablePanel);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(panel);
        this.add(scrollPane);
    }

    public void setVisible(int loc){
        this.setLocation(loc + 20, 0);
        this.setVisible(true);
    }
}
