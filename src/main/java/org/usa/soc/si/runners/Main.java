package org.usa.soc.si.runners;

import examples.si.AlgorithmFactory;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.action.EmptyAction;
import org.usa.soc.si.view.FunctionChartPlotter;
import org.usa.soc.si.view.FunctionDisplay;
import org.usa.soc.si.view.IterationChartPlotter;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class Main {

    /*
    UI components
     */
    JFrame frame;

    JPanel pnlCenter, pnlProgress, pnlRight, pnlLeft, pnlConfigs, pnlTop;
    RowPanel[] pnlDetails;
    JToolBar jToolBar;

    JLabel lblFunctionComboBox, lblAlgorithmComboBox, lblInterval, lblBestValue, lblExpectedBestValue, lblBestValueExpectedBestValueSep, labelStep;

    JComboBox cmbFunction, cmbAlgorithm;

    JSpinner spnInterval;

    JButton btnRun, btnShowTF, btnPause, btnStop, btnRepeate, btnStep;

    JProgressBar progressBar;

    XChartPanel<Chart<?, ?>> swarmDisplayChart;
    FunctionChartPlotter functionChartPlotter;

    int progressValue;
    int stepCount =0;
    int iterationCount, agentsCount, nd;

    double bestValue;

    IterationChartPlotter pltBestValue, pltConvergence, pltGradiantDecent, pltMeanBest;

    DecimalFormat decimalFormat;

    SIAlgorithm SIAlgorithm;

    public static boolean isRepeat = false;

    Thread currentRunner = null;

    private void runOptimizer(){
        currentRunner = new Thread(new Runnable() {
            @Override
            public void run() {

                btnRun.setEnabled(false);
                btnPause.setEnabled(!isRepeat);
                btnStop.setEnabled(true);
                btnRepeate.setEnabled(false);

                do{
                    int selectedAlgorithm = cmbAlgorithm.getSelectedIndex();
                    int selectedFunction = cmbFunction.getSelectedIndex();
                    int selectedInterval = (Integer) spnInterval.getValue();

                    SIAlgorithm = new AlgorithmFactory(selectedAlgorithm, AlgorithmFactory.FunctionsList.getFunctionList(nd)[selectedFunction]).getAlgorithm(iterationCount, agentsCount);
                    functionChartPlotter.setInterval(selectedInterval);
                    functionChartPlotter.setChart(SIAlgorithm);

                    clearValues();
                    pnlDetails[0].setTextValue(String.valueOf(new Date().getTime()));

                    functionChartPlotter.execute();
                    setInfoData(SIAlgorithm.toList());

                    if(!isRepeat){
                        btnRun.setEnabled(true);
                        btnPause.setEnabled(false);
                        btnStop.setEnabled(false);
                        btnStep.setEnabled(false);
                        btnRepeate.setEnabled(true);
                    }else{
                        selectedFunction++;
                        if(selectedFunction == cmbFunction.getItemCount()){
                            cmbFunction.setSelectedIndex(0);
                        }else{
                            cmbFunction.setSelectedIndex(selectedFunction);
                        }

                    }
                }while(isRepeat);
            }
        });

        currentRunner.start();
    }

    private void btnRunActionPerformed(ActionEvent e){

        btnRun.setEnabled(false);
        btnPause.setEnabled(true);
        btnStop.setEnabled(true);
        btnRepeate.setEnabled(false);
        btnStep.setEnabled(false);

        if(currentRunner != null && functionChartPlotter.isPaused()){
            functionChartPlotter.setInterval((Integer) spnInterval.getValue());
            functionChartPlotter.resume();
        }else{
            runOptimizer();
        }
    }

    private void btnPauseActionPerformed(ActionEvent e){
        if(currentRunner != null){
            btnRun.setEnabled(true);
            btnPause.setEnabled(false);
            btnStop.setEnabled(false);
            btnRepeate.setEnabled(false);
            btnStep.setEnabled(true);
            isRepeat = false;
            functionChartPlotter.pause();
        }
    }

    private void btnRepeatActionPerformed(ActionEvent e){
        btnRun.setEnabled(false);
        btnPause.setEnabled(false);
        btnStep.setEnabled(false);
        btnStop.setEnabled(true);
        btnRepeate.setEnabled(false);

        isRepeat = true;
        runOptimizer();
    }

    private void btnStopActionPerformed(ActionEvent e){
        if(currentRunner != null){
            btnRun.setEnabled(true);
            btnPause.setEnabled(false);
            btnStop.setEnabled(false);
            btnRepeate.setEnabled(true);
            functionChartPlotter.stopOptimizer();
            isRepeat = false;
            currentRunner=null;
        }
    }

    private void setInfoData(List<String> str) {
        pnlDetails[1].setTextValue(str.get(1));
        pnlDetails[2].setTextValue(str.get(2));
        pnlDetails[3].setTextValue(decimalFormat.format(Double.parseDouble(str.get(3))));
        pnlDetails[4].setTextValue(str.get(4));
        pnlDetails[5].setTextValue(str.get(7));
        pnlDetails[6].setTextValue(str.get(8));
        pnlDetails[7].setTextValue(decimalFormat.format(Double.parseDouble(str.get(11))));
        pnlDetails[8].setTextValue(decimalFormat.format(Double.parseDouble(str.get(12))));
        pnlDetails[9].setTextValue(decimalFormat.format(Double.parseDouble(str.get(13))));

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
        new FunctionDisplay(AlgorithmFactory.FunctionsList.getFunctionList(nd)[cmbFunction.getSelectedIndex()], 600, 600, 0, 0, true).display();
    }

    private void fncActionPerformed(double... values){

        progressValue = (int)values[0];
        bestValue = values[1];
        pltBestValue.addData(stepCount, bestValue);
        if(SIAlgorithm.getGradiantDecent() < 100000){
            pltGradiantDecent.addData(stepCount, SIAlgorithm.getGradiantDecent());
        }
        if(SIAlgorithm.getMeanBestValue() < 100000){
            pltMeanBest.addData(stepCount, SIAlgorithm.getMeanBestValue());
        }
        if(SIAlgorithm.getConvergenceValue() < 100000)
            pltConvergence.addData(stepCount, SIAlgorithm.getConvergenceValue());
        setInfoData(SIAlgorithm.toList());
        updateUI();
    }

    Main(){

        decimalFormat = new DecimalFormat("#.#######");
        this.init();

        functionChartPlotter =  new FunctionChartPlotter("Algorithm Viewer", 400, 400);
        SIAlgorithm SIAlgorithm = new AlgorithmFactory(0, AlgorithmFactory.FunctionsList.getFunctionList(nd)[0]).getAlgorithm(100, 100);
        functionChartPlotter.setChart(SIAlgorithm);

        swarmDisplayChart = new XChartPanel(functionChartPlotter.getChart());
        pnlCenter.add(swarmDisplayChart);

        pltBestValue = new IterationChartPlotter(300, 100, "", "Best Value", -1000);
        pnlRight.add(new XChartPanel(pltBestValue.getChart()));

        pltMeanBest = new IterationChartPlotter(300, 100, "", "Mean Best", -1000);
        pnlRight.add(new XChartPanel(pltMeanBest.getChart()));

        pltConvergence = new IterationChartPlotter(300, 100, "", "Convergence Value", -1000);
        pnlRight.add(new XChartPanel(pltConvergence.getChart()));

        pltGradiantDecent = new IterationChartPlotter(300, 100, "", "Gradiant Decent", -1000);
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
        lblExpectedBestValue.setText(decimalFormat.format(SIAlgorithm.getFunction().getExpectedBestValue()));
        lblExpectedBestValue.updateUI();
        swarmDisplayChart.updateUI();
        labelStep.setText(decimalFormat.format(SIAlgorithm.getCurrentStep()));
        labelStep.updateUI();
        pnlCenter.updateUI();
        pnlRight.updateUI();
        frame.repaint();
    }

    private void init() {

        Font f1 = new Font("SenSerif", Font.PLAIN, 16);
        Insets insets = new Insets(5,0,5,0);

        frame = new JFrame();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        jToolBar = new JToolBar();
        jToolBar.setMargin(insets);
        frame.add(jToolBar, BorderLayout.NORTH);

        lblAlgorithmComboBox = new JLabel("Algorithm: ");
        lblAlgorithmComboBox.setFont(f1);
        cmbAlgorithm = new JComboBox<>(AlgorithmFactory.generateAlgo().toArray(new String[0]));
        cmbAlgorithm.setFont(f1);
        jToolBar.add(lblAlgorithmComboBox);
        jToolBar.add(cmbAlgorithm);

        jToolBar.addSeparator();

        lblFunctionComboBox = new JLabel("Test Function: ");
        lblFunctionComboBox.setFont(f1);
        cmbFunction = new JComboBox<>();
        cmbFunction.setFont(f1);
        for (ObjectiveFunction f: AlgorithmFactory.FunctionsList.getFunctionList(nd)) {
            cmbFunction.addItem(f.getClass().getSimpleName());
        }
        jToolBar.add(lblFunctionComboBox);
        jToolBar.add(cmbFunction);

        jToolBar.addSeparator();

        lblInterval = new JLabel("Interval :");
        lblInterval.setFont(f1);

        spnInterval = new JSpinner();
        spnInterval.setFont(f1);
        spnInterval.setValue(150);

        jToolBar.add(lblInterval);
        jToolBar.add(spnInterval);

        jToolBar.addSeparator();

        btnRun = new JButton("Run");
        btnRun.setFont(f1);
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRunActionPerformed(e);
            }
        });
        jToolBar.add(btnRun);

        btnPause = new JButton("Pause");
        btnPause.setFont(f1);
        btnPause.setEnabled(false);
        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnPauseActionPerformed(e);
            }
        });
        jToolBar.add(btnPause);

        btnStep = new JButton("+ Step");
        btnStep.setFont(f1);
        btnStep.setEnabled(false);
        btnStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                functionChartPlotter.stepOver();
            }
        });
        jToolBar.add(btnStep);

        btnStop = new JButton("Stop");
        btnStop.setFont(f1);
        btnStop.setEnabled(false);
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStopActionPerformed(e);
            }
        });
        jToolBar.add(btnStop);

        btnRepeate = new JButton("Repeat");
        btnRepeate.setFont(f1);
        btnRepeate.setEnabled(true);
        btnRepeate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRepeatActionPerformed(e);
            }
        });
        jToolBar.add(btnRepeate);


        jToolBar.addSeparator();

        btnShowTF = new JButton("Show TF");
        btnShowTF.setFont(f1);
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

        labelStep = new JLabel("0", JLabel.CENTER);
        labelStep.setForeground(Color.BLACK);

        pnlProgress = new JPanel();
        pnlProgress.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.add(lblBestValue);
        jPanel.add(lblBestValueExpectedBestValueSep);
        jPanel.add(lblExpectedBestValue);
        jPanel.add(labelStep);
        pnlProgress.add(jPanel, BorderLayout.WEST);
        pnlProgress.add(progressBar, BorderLayout.CENTER);
        frame.add(pnlProgress, BorderLayout.SOUTH);

        pnlTop = new JPanel();

        pnlRight = new JPanel();
        pnlRight.setLayout(new GridLayout(4,1));

        pnlLeft = new JPanel();
        pnlLeft.setLayout(new GridLayout(10,1));

        generatePanelList();

        for(JPanel p : pnlDetails){
            if(p!=null){
                pnlLeft.add(p);
            }
        }

        RowPanel iterationPanel = new RowPanel(" Iterations", "100");
        iterationCount = 100;
        iterationPanel.txt.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(!iterationPanel.txt.getText().isEmpty()){
                    iterationCount = Integer.parseInt(iterationPanel.txt.getText());
                }
            }
        });
        pnlTop.add(iterationPanel);

        RowPanel pnlAgentsCount = new RowPanel(" Agents Count", "100");
        agentsCount = 100;
        pnlAgentsCount.txt.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(!pnlAgentsCount.txt.getText().isEmpty()){
                    agentsCount = Integer.parseInt(pnlAgentsCount.txt.getText());
                }
            }
        });
        pnlTop.add(pnlAgentsCount);

        RowPanel pnlNumberOfDimentions = new RowPanel(" Dimentions", "2");
        nd = 2;
        pnlNumberOfDimentions.txt.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(!pnlNumberOfDimentions.txt.getText().isEmpty()){
                    nd = Integer.parseInt(pnlNumberOfDimentions.txt.getText());
                }
            }
        });
        pnlTop.add(pnlNumberOfDimentions);

        Panel body = new Panel();
        body.setLayout(new BorderLayout());

        body.add(pnlTop, BorderLayout.NORTH);
        body.add(pnlRight, BorderLayout.EAST);
        body.add(pnlLeft, BorderLayout.WEST);
        pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout());
        body.add(pnlCenter, BorderLayout.CENTER);

        frame.add(body, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void generatePanelList() {
        pnlDetails = new RowPanel[15];

        pnlDetails[0]=new RowPanel("Test ID", "N/A", false);
        pnlDetails[1]=new RowPanel("Algorithm Name", "N/A", false);
        pnlDetails[2]=new RowPanel("Test Function", "N/A", false);
        pnlDetails[3]=new RowPanel("Best Value", "N/A", false);
        pnlDetails[4]=new RowPanel("Expected Value", "N/A", false);
        pnlDetails[5]=new RowPanel("Number of Dimensions", "N/A", false);
        pnlDetails[6]=new RowPanel("Execution Time (ms)", "N/A", false);
        pnlDetails[7]=new RowPanel("Convergence", "N/A", false);
        pnlDetails[8]=new RowPanel("Gradiant Decent", "N/A", false);
        pnlDetails[9]=new RowPanel("Mean Best Value", "N/A", false);
    }

    class RowPanel extends JPanel{
        String key, value;
        JTextField txt;

        public RowPanel(String key, String value){
            setValues(key, value);
        }

        private void setValues(String key, String value){
            this.key = key;
            this.value = value;
            this.setLayout(new GridLayout(1,2));
            this.add(new JLabel(key));

            txt = new JTextField();
            txt.setText(value);
            this.add(txt);
        }

        public RowPanel(String key, String value, boolean enableText){
            setValues(key, value);
            txt.setEditable(enableText);
        }

        public void setTextValue(String v){
            this.txt.setText(v);
        }
    }

    public static void executeMain(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}
