package org.usa.soc.surfacePlotter;

import org.usa.soc.ObjectiveFunction;
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

    ObjectiveFunction[] fns = new ObjectiveFunction[]{
            new AckleysFunction(),
            new BealeFunction(),
            new BoothsFunction(),
            new BukinFunction(),
            new CrossInTrayFunction(),
            new EasomFunction(),
            new EggholderFunction(),
            new GoldsteinPrice(),
            new HimmelblausFunction(),
            new HolderTableFunction(),
            new LevyFunction(),
            new MatyasFunction(),
            new McCormickFunction(),
            new RastriginFunction(),
            new RosenbrockFunctionDish(),
            new SchafferFunctionN4(),
            new SchafferFunction(),
            new SphereFunction(),
            new StyblinskiTangFunction(),
            new ThreeHumpCamelFunction()
    };

    private void setPloat(ObjectiveFunction fn){
        p = new Plot(new FunctionToMapper(fn),"Function Display", true);
    }

    public FunctionDisplay() {

        JFrame jf = new JFrame("sample");
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
}
