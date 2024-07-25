package org.usa.soc.si.runners;

import examples.si.AlgorithmFactory;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.view.surface.plotter.FunctionToMapper;
import org.usa.soc.view.surface.plotter.Plot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
This is only for displaying function
 */

public class FunctionDisplay {
    Plot p;

    ObjectiveFunction fns[] = new AlgorithmFactory.FunctionsList().getFunctionList();

    private void setPloat(ObjectiveFunction fn){
        p = new Plot(new FunctionToMapper(fn),"Function Display", true);
    }

    public FunctionDisplay() {

        JFrame jf = new JFrame("Functions");
        JToolBar toolBar = new JToolBar();
        JComboBox b = new JComboBox();

        setPloat(fns[0]);

        for (ObjectiveFunction f: fns) {
            b.addItem(f.getClass().getSimpleName());
        }

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPloat(fns[b.getSelectedIndex()]);
                jf.getContentPane().remove(1);
                jf.getContentPane().add(p);
                jf.getContentPane().revalidate();
                jf.getContentPane().repaint();
            }
        });

        toolBar.add(b);

        jf.add(toolBar, BorderLayout.NORTH);
        jf.setSize(650, 650);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.getContentPane().add(p);
        jf.setVisible(true);
    }

    public FunctionDisplay(int y) {

        JFrame jf = new JFrame("Functions");

        setPloat(fns[y]);

        jf.setSize(700, 700);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.getContentPane().add(p);
        jf.setVisible(true);
    }
}