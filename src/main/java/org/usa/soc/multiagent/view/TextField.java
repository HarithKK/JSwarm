package org.usa.soc.multiagent.view;

import javax.swing.*;
import java.awt.*;

public class TextField extends DataBox {

    JTextField textField;
    JLabel label;

    public TextField(String key, JTextField txt){
        this.textField = txt;
        this.label = new JLabel();
        label.setText(key);
    }

    public TextField(String key){
        this.textField = new JTextField();
        this.label = new JLabel();
        label.setText(key);
    }

    public String getKey(){
        return label.getText();
    }

    public Component getPanel() {
        Panel p = new Panel();
        p.add(label);
        p.add(textField);
        return p;
    }

    public void setData(String value) {
        this.textField.setText(value);
    }
}
