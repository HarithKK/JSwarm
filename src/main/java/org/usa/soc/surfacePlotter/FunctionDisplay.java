package org.usa.soc.surfacePlotter;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.FunctionsList;
import org.usa.soc.benchmarks.singleObjective.*;
import org.usa.soc.benchmarks.singleObjectiveConstrained.RosenbrockFunctionDish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
This is only for displaying function
 */

public class FunctionDisplay {
    Plot p;

    ObjectiveFunction fns[] = new FunctionsList().getFunctionList();

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
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(p);
        jf.setVisible(true);
    }

    public FunctionDisplay(int y) {

        JFrame jf = new JFrame("Functions");

        setPloat(fns[y]);

        jf.setSize(500, 500);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(p);
        jf.setVisible(true);
    }
}
