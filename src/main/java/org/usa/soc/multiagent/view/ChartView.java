package org.usa.soc.multiagent.view;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;
import org.usa.soc.core.exceptions.KillOptimizerException;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.core.Flag;
import org.usa.soc.core.action.Action;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class ChartView extends JFrame {

    DecimalFormat decimalFormat;

    JToolBar jToolBar;
    JButton btnRun, btnPause, btnStop, btnRepeat;
    JProgressBar progressBar;

    JLabel labelStep;

    JPanel pnlProgress, pnlCenter;

    PlainView2D view2D;

    XChartPanel<Chart<?, ?>> chartPanel;

    private int progressValue;

    Thread currentRunner = null;

    Flag isRepeat = new Flag();
    public ChartView(String title, Algorithm algorithm, int w, int h, Margins m){

        setTitle(title);
        setSize(w, h);

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

    private void updateUI() {
        progressBar.setValue(progressValue);
        labelStep.setText(progressValue + "%");
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
                btnRepeat.setEnabled(false);

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
        btnRepeat.setEnabled(false);

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
            btnRepeat.setEnabled(false);
            isRepeat.unset();
            view2D.pauseExecution();
        }
    }

    private void btnRepeatActionPerformed(ActionEvent e){
        btnRun.setEnabled(false);
        btnPause.setEnabled(false);
        btnStop.setEnabled(true);
        btnRepeat.setEnabled(false);
        isRepeat.set();
        runOptimizer();
    }

    private void btnStopActionPerformed(ActionEvent e){
        if(currentRunner != null){
            btnRun.setEnabled(true);
            btnPause.setEnabled(false);
            btnStop.setEnabled(false);
            btnRepeat.setEnabled(true);
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

        btnRepeat = new JButton("Repeat");
        btnRepeat.setFont(f1);
        btnRepeat.setEnabled(true);
        btnRepeat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRepeatActionPerformed(e);
            }
        });
        //jToolBar.add(btnRepeat);

        progressBar = new JProgressBar();
        progressBar.setValue(progressValue);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);

        labelStep = new JLabel("0", JLabel.CENTER);
        labelStep.setForeground(Color.BLACK);

        pnlProgress = new JPanel();
        pnlProgress.setLayout(new BorderLayout());
        pnlProgress.add(progressBar, BorderLayout.CENTER);
        add(pnlProgress, BorderLayout.SOUTH);

        pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout());
        add(pnlCenter, BorderLayout.CENTER);

        this.setVisible(true);
    }

}
