package org.usa.soc.si.view;

import examples.si.AlgorithmFactory;
import org.usa.soc.si.ObjectiveFunction;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class Plain2D extends JFrame {

    private DecimalFormat decimalFormat = new DecimalFormat("#.#######");
    private Font f1 = new Font("SenSerif", Font.PLAIN, 16);
    private Insets insets = new Insets(5,0,5,0);

    private JToolBar jToolBar;

    private JLabel lblAlgorithmComboBox, lblFunctionComboBox;
    private JComboBox cmbAlgorithm, cmbFunction;

    public Plain2D(){

        this.initComponents();
    }

    private void initComponents() {
        Font f1 = new Font("SenSerif", Font.PLAIN, 16);

        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        jToolBar = new JToolBar();
        jToolBar.setMargin(insets);
        add(jToolBar, BorderLayout.NORTH);

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

        for (ObjectiveFunction f: AlgorithmFactory.FunctionsList.getFunctionList()) {
            cmbFunction.addItem(f.getClass().getSimpleName());
        }

        jToolBar.add(lblFunctionComboBox);
        jToolBar.add(cmbFunction);
    }
}
