package org.usa.soc.view.surface.plotter;

import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.Mapper;
import net.ericaro.surfaceplotter.ProgressiveSurfaceModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Plot extends JPanel {

    private final ProgressiveSurfaceModel model;
    private JSurfacePanel panel;

    public Plot(Mapper m, String title, boolean configVisible) {

        initComponents(title, configVisible);

        model = new ProgressiveSurfaceModel();
        panel.setModel(model);
        model.setMapper(m);
        model.plot().execute();
        model.setPlotFunction12(true, false);

    }

    private void initComponents(String title, boolean c) {

        setLayout(new BorderLayout());

        panel = new JSurfacePanel();
        panel.setTitleText(title);
        panel.setBackground(Color.white);
        panel.setTitleFont(panel.getTitleFont().deriveFont(panel.getTitleFont().getStyle() | Font.BOLD, panel.getTitleFont().getSize() + 6f));
        panel.setConfigurationVisible(c);
        add(panel, BorderLayout.CENTER);

        JButton button = new JButton();
        button.setText("Export SVG");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                try {
                    if (jfc.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION)
                        panel.getSurface().doExportPNG(jfc.getSelectedFile());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Export Error!");
                }
            }
        });

        JSlider slider = new JSlider();
        slider.setMaximum(6);
        slider.setValue(0);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting())
                    model.setCurrentDefinition(slider.getValue());
            }
        });

        JToolBar toolBar = new JToolBar();
        toolBar.add(button);
        toolBar.add(slider);

        add(toolBar, BorderLayout.NORTH);

    }

    public void execute(boolean autoPack) {
        JFrame jf = new JFrame("sample");
        jf.setSize(650, 650);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(this);
        if (autoPack) {
            jf.pack();
        }
        jf.setVisible(true);
    }

    public void execute(){
        model.plot().execute();
    }
}
