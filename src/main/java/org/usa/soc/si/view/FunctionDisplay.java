package org.usa.soc.si.view;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.view.plotter.FunctionToMapper;
import org.usa.soc.si.view.plotter.Plot;

import javax.swing.*;

/*
This is only for displaying function
 */

public class FunctionDisplay extends JFrame {
    Plot p;

    public FunctionDisplay(ObjectiveFunction function, int w, int h, int x, int y, boolean configs) {

        this.setTitle(function.getClass().getSimpleName());
        this.setSize(w, h);
        this.setLocation(x, y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p = new Plot(new FunctionToMapper(function),getTitle(), configs);
        this.getContentPane().add(p);
    }

    public FunctionDisplay display(){
        this.setVisible(true);
        return this;
    }
}