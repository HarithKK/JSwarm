package org.usa.soc.multiagent.view;

import javax.swing.*;
import java.awt.*;

public class DataView extends JFrame{

    ProgressiveChart chart;

    JPanel panel;

    int s =0;

    public DataView(int x){
        setTitle("Data View");
        this.setSize(300, 500);
        this.setLocation(x+20, 0);
        initialize();
    }

    public void addData(double v){
        chart.addData(s++, v);
        chart.updateUI();
        panel.updateUI();
        this.revalidate();
        this.repaint();
    }

    private void initialize() {
        panel = new JPanel();

        chart = new ProgressiveChart(300, 200, "T1", "s1", "it", 0);
        chart.setMaxLength(100);
        panel.add(chart.getPanel());

        this.add(panel);
        this.setVisible(true);
    }
}
