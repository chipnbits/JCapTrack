package ui.gui;

import javax.swing.*;

public class TransactionEntryPanel extends JPanel {

    JTextField xField = new JTextField(5);
    JTextField yField = new JTextField(5);

    public TransactionEntryPanel() {

        this.add(new JLabel("x:"));
        this.add(xField);
        this.add(Box.createHorizontalStrut(15)); // a spacer
        this.add(new JLabel("y:"));
        this.add(yField);

        int result = JOptionPane.showConfirmDialog(null, this,
                "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println("x value: " + xField.getText());
            System.out.println("y value: " + yField.getText());
        }
    }

}
