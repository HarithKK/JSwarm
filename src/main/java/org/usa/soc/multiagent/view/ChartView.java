package org.usa.soc.multiagent.view;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.core.Flag;
import org.usa.soc.core.action.Action;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.util.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChartView extends JFrame {

    DecimalFormat decimalFormat;

    JToolBar jToolBar;
    JButton btnRun, btnPause, btnStop;
    JProgressBar progressBar;

    JLabel labelStep, lblInterval;

    JPanel pnlProgress, pnlCenter;
    JSlider sldInterval;
    PlainView2D view2D;

    XChartPanel<Chart<?, ?>> chartPanel;

    private int progressValue;

    Thread currentRunner = null;

    Flag isRepeat = new Flag();
    public ChartView(String title, Algorithm algorithm, int w, int h, Margins m){

        setTitle(title);
        setSize(w, h);
        algorithm.setMargins(m);

        decimalFormat = new DecimalFormat("#.#######");

        this.initComponents();

        view2D = new PlainView2D(algorithm.getName(),w, h, algorithm, m);
        view2D.setInterval(algorithm.getInterval());

        chartPanel = new XChartPanel<>(view2D.getChart());
        pnlCenter.add(chartPanel);

        view2D.setAction(new Action() {
            @Override
            public void performAction(double percentage) {
                progressValue = (int)percentage;
                if(progressValue > 100){
                    progressValue = 0;
                }
                updateUI();
            }
        });
    }

    public void setInterval(int delay){
        this.view2D.setInterval(delay);
    }

    private void updateUI() {
        progressBar.setValue(progressValue);
        labelStep.setText(String.valueOf(this.view2D.getAlgo().getCurrentStep()));
        labelStep.updateUI();
        pnlCenter.updateUI();
    }

    private void runOptimizer(){
        currentRunner = new Thread(new Runnable() {
            @Override
            public void run() {

                btnRun.setEnabled(false);
                btnPause.setEnabled(!isRepeat.isSet());
                btnStop.setEnabled(true);

                clearValues();
                try{
                    view2D.execute();
                }
                catch (Exception e){
                    e.printStackTrace();
                    Logger.getInstance().error(e.getMessage());
                }
            }
        });

        currentRunner.start();
    }

    private void clearValues() {
        progressValue = 0;
    }

    private void btnRunActionPerformed(ActionEvent e){

        btnRun.setEnabled(false);
        btnPause.setEnabled(true);
        btnStop.setEnabled(true);

        if(currentRunner != null && view2D.getAlgo().isPaused()){
            view2D.getAlgo().resumeOptimizer();
        }else{
            runOptimizer();
        }
    }

    private void btnPauseActionPerformed(ActionEvent e){
        if(currentRunner != null){
            btnRun.setEnabled(true);
            btnPause.setEnabled(false);
            btnStop.setEnabled(false);
            isRepeat.unset();
            view2D.pauseExecution();
        }
    }

    private void btnStopActionPerformed(ActionEvent e){
        if(currentRunner != null){
            btnRun.setEnabled(true);
            btnPause.setEnabled(false);
            btnStop.setEnabled(false);
            view2D.getAlgo().stopOptimizer();
            view2D.stopExecution();
            isRepeat.unset();
            currentRunner=null;
            System.exit(0);
        }
    }

    private void initComponents() {

        Font f1 = new Font("SenSerif", Font.PLAIN, 16);
        Insets insets = new Insets(5,0,5,0);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        jToolBar = new JToolBar();
        jToolBar.setMargin(insets);
        this.add(jToolBar, BorderLayout.NORTH);

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

        jToolBar.add(new JToolBar.Separator());
        lblInterval = new JLabel("Interval: 100", JLabel.CENTER);
        jToolBar.add(lblInterval);
        sldInterval = new JSlider(10,3000, 100);
        sldInterval.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblInterval.setText("Interval: "+sldInterval.getValue());
                setInterval(sldInterval.getValue());
            }
        });
        jToolBar.add(sldInterval);

        progressBar = new JProgressBar();
        progressBar.setValue(progressValue);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);

        JLabel labelStepTitle = new JLabel("Step: ", JLabel.LEFT);
        labelStepTitle.setForeground(Color.BLACK);

        labelStep = new JLabel("0", JLabel.CENTER);
        labelStep.setForeground(Color.BLACK);

        pnlProgress = new JPanel();
        pnlProgress.add(labelStepTitle);
        pnlProgress.add(labelStep);
        add(pnlProgress, BorderLayout.SOUTH);

        pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout());
        add(pnlCenter, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public void setCustomActions(List<Component> customActions) {
        for(Component btn: customActions){
            this.jToolBar.add(btn);
        }
    }
}
