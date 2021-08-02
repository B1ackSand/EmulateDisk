package gui;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 */
public class Help extends JDialog {

    private JDialog jd1;
    private JLabel jl;

    public Help(Component c) {
        jd1 = this;
        this.setTitle("帮助");
        this.setSize(180, 130);
        this.setLayout(new FlowLayout());
        this.setModal(true);
        this.setLocationRelativeTo(c);
        this.addJLabel();
        this.setVisible(true);
    }

    private void addJLabel() {
        jl = new JLabel();

        String text = "这是一个帮助。";
        jl.setText(text);
        this.add(jl);
    }
}
