package org.usa.soc.multiagent.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Button extends DataBox{

    private JButton jb;
    public Button(String key){
        jb = new JButton();
        jb.setText(key);
    }

    public Button(String key, JButton j){
        jb = j;
        jb.setText(key);
    }

    public String getKey(){
        return jb.getText();
    }

    public Component getPanel() {
        return jb;
    }

    public Button addAction(ActionListener l){
        jb.addActionListener(l);
        return this;
    }
}
