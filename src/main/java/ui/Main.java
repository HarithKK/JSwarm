package ui;

import org.checkerframework.checker.units.qual.A;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;
import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.FunctionsList;
import org.usa.soc.core.EmptyAction;
import soc.usa.display.FunctionChartPlotter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Main {

    JFrame frame;

    JLabel jLabel4;

    JButton jButton;

    int jpValue = 0;
    double value =0;

    FunctionChartPlotter functionChartPlotter;

    ObjectiveFunction fns[] = new FunctionsList().getFunctionList();

    DecimalFormat decimalFormat;

    Main(){
        frame = new JFrame();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        decimalFormat = new DecimalFormat("#.###");

        JToolBar jToolBar = new JToolBar();

        JLabel jLabel = new JLabel("Algorithm: ");
        JComboBox jComboBox = new JComboBox<>(AlgoStore.generateAlgo().toArray(new String[0]));
        jToolBar.add(jLabel);
        jToolBar.add(jComboBox);

        JLabel jLabel1 = new JLabel("Test Function: ");
        JComboBox jComboBox1 = new JComboBox<>();
        for (ObjectiveFunction f: fns) {
            jComboBox1.addItem(f.getClass().getSimpleName());
        }

        jToolBar.addSeparator();
        jToolBar.add(jLabel1);
        jToolBar.add(jComboBox1);

        JLabel jLabel2 = new JLabel("Interval :");
        JSpinner jSpinner = new JSpinner();
        jSpinner.setValue(50);

        jToolBar.addSeparator();
        jToolBar.add(jLabel2);
        jToolBar.add(jSpinner);

        jButton = new JButton("Run");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Algorithm algorithm = new AlgoStore(jComboBox.getSelectedIndex(), fns[jComboBox1.getSelectedIndex()]).getAlgorithm();
                functionChartPlotter.setTime((Integer) jSpinner.getValue());
                functionChartPlotter.setChart(algorithm);
                jpValue=0;
                jLabel4.setText("Expected Best Value: "+ algorithm.getFunction().getExpectedBestValue());
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        jButton.setEnabled(false);
                        functionChartPlotter.execute();
                        jButton.setEnabled(true);
                    }
                }).start();
            }
        });

        JButton jButton1 = new JButton("Show TF");
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new org.usa.soc.surfacePlotter.FunctionDisplay(jComboBox1.getSelectedIndex());
            }
        });

        jToolBar.addSeparator();
        jToolBar.add(jButton);
        jToolBar.add(jButton1);

        JProgressBar jp = new JProgressBar();
        jp.setMaximum(100);
        jp.setMinimum(0);
        jp.setValue(jpValue);

        JLabel jLabel3 = new JLabel("Best Value: "+value);
        jLabel4 = new JLabel("Expected Best Value: "+value);

        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new GridLayout(3,1));
        jPanel1.add(jp);
        jPanel1.add(jLabel3);
        jPanel1.add(jLabel4);

        frame.getContentPane().add(jPanel1, BorderLayout.SOUTH);

        functionChartPlotter =  new FunctionChartPlotter("Algorithm Viewer", 600, 600);
        Algorithm algorithm = new AlgoStore(0, fns[0]).getAlgorithm();
        functionChartPlotter.setChart(algorithm);
        XChartPanel<Chart<?, ?>> chartPanel = new XChartPanel(functionChartPlotter.getChart());

        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        functionChartPlotter.setAction(new EmptyAction() {
            @Override
            public void performAction(double d, double d1) {
                jpValue = (int)d;
                value = d1;
                jp.setValue(jpValue);
                jLabel3.setText("  Best Value: "+decimalFormat.format(value));
                chartPanel.updateUI();
                frame.repaint();
            }
        });


        frame.add(jToolBar, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}
