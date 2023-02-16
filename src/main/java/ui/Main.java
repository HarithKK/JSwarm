package ui;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;
import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.FunctionsList;
import org.usa.soc.core.EmptyAction;
import soc.usa.display.FunctionChartPlotter;
import soc.usa.display.IterationChartPlotter;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Main {

    /*
    UI components
     */
    JFrame frame;

    JPanel pnlCenter, pnlProgress, pnlRight, pnlLeft, pnlConfigs;
    JToolBar jToolBar;

    JLabel lblFunctionComboBox, lblAlgorithmComboBox, lblInterval, lblBestValue, lblExpectedBestValue, lblBestValueExpectedBestValueSep;

    JComboBox cmbFunction, cmbAlgorithm;

    JTextPane infoData;

    JSpinner spnInterval;

    JButton btnRun, btnShowTF;

    JProgressBar progressBar;

    XChartPanel<Chart<?, ?>> swarmDisplayChart;
    FunctionChartPlotter functionChartPlotter;

    int progressValue;
    int stepCount =0;
    int iterationCount, agentsCount;

    double bestValue;


    IterationChartPlotter pltBestValue, pltConvergence, pltGradiantDecent, pltMeanBest;

    ObjectiveFunction fns[] = new FunctionsList().getFunctionList();

    DecimalFormat decimalFormat;

    Algorithm algorithm;

    private void btnRunActionPerformed(ActionEvent e){
        int selectedAlgorithm = cmbAlgorithm.getSelectedIndex();
        int selectedFunction = cmbFunction.getSelectedIndex();
        int selectedInterval = (Integer) spnInterval.getValue();

        algorithm = new AlgoStore(selectedAlgorithm, fns[selectedFunction]).getAlgorithm(iterationCount, agentsCount);
        functionChartPlotter.setTime(selectedInterval);
        functionChartPlotter.setChart(algorithm);

        clearValues();

        new Thread(new Runnable() {
            @Override
            public void run() {
                btnRun.setEnabled(false);
                functionChartPlotter.execute();
                btnRun.setEnabled(true);
                infoData.setText(algorithm.toString());
            }
        }).start();
    }

    private void clearValues() {
        progressValue = 0;
        stepCount=0;
        pltBestValue.clearChart();
        pltConvergence.clearChart();
        pltGradiantDecent.clearChart();
        pltMeanBest.clearChart();
    }

    private void btnShowTFActionPerformed(ActionEvent e){
        new org.usa.soc.surfacePlotter.FunctionDisplay(cmbFunction.getSelectedIndex());
    }

    private void fncActionPerformed(double... values){
        progressValue = (int)values[0];
        bestValue = values[1];
        pltBestValue.addData(stepCount, bestValue);
        if(algorithm.getGradiantDecent() < 100000){
            pltGradiantDecent.addData(stepCount, algorithm.getGradiantDecent());
        }
        if(algorithm.getMeanBestValue() < 100000){
            pltMeanBest.addData(stepCount, algorithm.getMeanBestValue());
        }
        if(algorithm.getConvergenceValue() < 100000)
            pltConvergence.addData(stepCount, algorithm.getConvergenceValue());
        infoData.setText(algorithm.toString());
        updateUI();
    }

    Main(){

        decimalFormat = new DecimalFormat("#.###");
        this.init();

        functionChartPlotter =  new FunctionChartPlotter("Algorithm Viewer", 400, 400);
        Algorithm algorithm = new AlgoStore(0, fns[0]).getAlgorithm(100, 100);
        functionChartPlotter.setChart(algorithm);

        swarmDisplayChart = new XChartPanel(functionChartPlotter.getChart());
        pnlCenter.add(swarmDisplayChart);

        pltBestValue = new IterationChartPlotter(600, 100, "", "Best Value", -1000);
        pnlRight.add(new XChartPanel(pltBestValue.getChart()));

        pltMeanBest = new IterationChartPlotter(600, 100, "", "Mean Best", -1000);
        pnlRight.add(new XChartPanel(pltMeanBest.getChart()));

        pltConvergence = new IterationChartPlotter(600, 100, "", "Convergence Value", -1000);
        pnlRight.add(new XChartPanel(pltConvergence.getChart()));

        pltGradiantDecent = new IterationChartPlotter(600, 100, "", "Gradiant Decent", -1000);
        pnlRight.add(new XChartPanel(pltGradiantDecent.getChart()));

        functionChartPlotter.setAction(new EmptyAction() {
            @Override
            public void performAction(int step, double... values) {
                stepCount = step;
                fncActionPerformed(values);
            }
        });
    }

    private void updateUI() {

        progressBar.setValue(progressValue);
        lblBestValue.setText(decimalFormat.format(bestValue));
        lblExpectedBestValue.setText(decimalFormat.format(algorithm.getFunction().getExpectedBestValue()));
        lblExpectedBestValue.updateUI();
        swarmDisplayChart.updateUI();
        pnlCenter.updateUI();
        frame.repaint();
    }

    private void init() {

        frame = new JFrame();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        jToolBar = new JToolBar();
        frame.add(jToolBar, BorderLayout.NORTH);

        lblAlgorithmComboBox = new JLabel("Algorithm: ");
        cmbAlgorithm = new JComboBox<>(AlgoStore.generateAlgo().toArray(new String[0]));
        jToolBar.add(lblAlgorithmComboBox);
        jToolBar.add(cmbAlgorithm);

        jToolBar.addSeparator();

        lblFunctionComboBox = new JLabel("Test Function: ");
        cmbFunction = new JComboBox<>();
        for (ObjectiveFunction f: fns) {
            cmbFunction.addItem(f.getClass().getSimpleName());
        }
        jToolBar.add(lblFunctionComboBox);
        jToolBar.add(cmbFunction);

        jToolBar.addSeparator();

        lblInterval = new JLabel("Interval :");
        spnInterval = new JSpinner();
        spnInterval.setValue(50);

        jToolBar.add(lblInterval);
        jToolBar.add(spnInterval);

        jToolBar.addSeparator();

        btnRun = new JButton("Run");
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRunActionPerformed(e);
            }
        });
        jToolBar.add(btnRun);

        jToolBar.addSeparator();

        btnShowTF = new JButton("Show TF");
        btnShowTF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnShowTFActionPerformed(e);
            }
        });
        jToolBar.add(btnShowTF);

        progressBar = new JProgressBar();
        progressBar.setValue(progressValue);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);

        lblBestValue = new JLabel("0.0", JLabel.CENTER);
        lblBestValue.setForeground(Color.RED);
        lblBestValueExpectedBestValueSep = new JLabel(">", JLabel.CENTER);
        lblExpectedBestValue = new JLabel("0.0", JLabel.CENTER);
        lblExpectedBestValue.setForeground(Color.BLUE);

        pnlProgress = new JPanel();
        pnlProgress.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.add(lblBestValue);
        jPanel.add(lblBestValueExpectedBestValueSep);
        jPanel.add(lblExpectedBestValue);
        pnlProgress.add(jPanel, BorderLayout.WEST);
        pnlProgress.add(progressBar, BorderLayout.CENTER);

        pnlRight = new JPanel();
        pnlRight.setLayout(new GridLayout(4,1));

        pnlLeft = new JPanel();
        pnlLeft.setLayout(new GridLayout(2,1));

        infoData = new JTextPane();
        infoData.setText("Info Data");
        pnlLeft.add(infoData);

        pnlConfigs = getPanelConfigs();
        pnlLeft.add(pnlConfigs);

        frame.add(pnlRight, BorderLayout.EAST);
        frame.add(pnlLeft, BorderLayout.WEST);
        frame.getContentPane().add(pnlProgress, BorderLayout.SOUTH);

        pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout());
        frame.add(pnlCenter, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel getPanelConfigs() {

        JPanel jp = new JPanel();
        GridBagLayout d = new GridBagLayout();
        jp.setLayout(d);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill =  GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = GridBagConstraints.PAGE_START;

        RowPanel rowPanel1 = new RowPanel(" Iterations", "100");
        iterationCount = 100;
        rowPanel1.txt.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(!rowPanel1.txt.getText().isEmpty()){
                    iterationCount = Integer.parseInt(rowPanel1.txt.getText());
                }
            }
        });
        jp.add(rowPanel1, gbc);

        gbc.gridy = 1;
        RowPanel rowPanel2 = new RowPanel(" Agents Count", "100");
        agentsCount = 100;
        rowPanel2.txt.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(!rowPanel2.txt.getText().isEmpty()){
                    agentsCount = Integer.parseInt(rowPanel2.txt.getText());
                }
            }
        });
        jp.add(rowPanel2, gbc);

        return jp;

    }

    class RowPanel extends JPanel{
        String key, value;
        JTextField txt;

        public RowPanel(String key, String value){
            this.key = key;
            this.value = value;
            this.setLayout(new GridLayout(1,2));
            this.add(new JLabel(key));

            txt = new JTextField();
            txt.setText(value);
            this.add(txt);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}
