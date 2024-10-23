package org.usa.soc.multiagent.view;

import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.util.Commons;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Vector;

public class Table extends DataBox {

    JTable table;
    JLabel label;

    DecimalFormat formatter = new DecimalFormat("0.#E0");

    public Table(String key, JTable txt){
        this.table = new JTable();
        this.label = new JLabel();
        label.setText(key);
    }

    public Table(String key, int row, int column){
        this.table = new JTable();
        DefaultTableModel tm = (DefaultTableModel) this.table.getModel();
        for(int i =0; i< column; i++){
            tm.addColumn(i);
        }
        for(int i =0; i< row; i++){
            tm.addRow(new Vector(column));
        }
        this.label = new JLabel();
        label.setText(key);
    }

    public String getKey(){
        return label.getText();
    }

    public Component getPanel() {
        Panel p = new Panel();
        p.setLayout(new BorderLayout());
        p.add(label, BorderLayout.NORTH);
        p.add(table, BorderLayout.CENTER);
        return p;
    }

    public void setData(RealMatrix value) {
        DefaultTableModel tm = (DefaultTableModel) this.table.getModel();
        for(int i=0; i< value.getRowDimension(); i++){
            for(int j=0; j< value.getColumnDimension(); j++){
                tm.setValueAt(formatter.format(value.getEntry(i, j)), i, j);
            }
        }
    }
}
