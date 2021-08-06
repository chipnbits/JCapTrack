package ui.gui;

import model.Portfolio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PortfolioNavigatorMenu extends MenuFrame {

    private static final int HEIGHT = 640;
    private static final int WIDTH = 640;
    private final Portfolio user;

    // EFFECTS: makes a new frame in the default style of JCapTrackMenu
    public PortfolioNavigatorMenu(Portfolio p) {
        super(p.getName());
        user = p;
        this.setTitle(p.getName());
        setup();
        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
    }

    private void setup() {
        ImageIcon icon = new ImageIcon(ICON_LOCATION);
        this.setIconImage(icon.getImage());

        this.setSize(WIDTH, HEIGHT);  // Set the dimensions
        this.setResizable(false);
        this.setVisible(true);
        setLocationRelativeTo(null);
    }


    @Override
    protected void closePrompt() {
        Toolkit.getDefaultToolkit().beep();
        int answer = JOptionPane.showConfirmDialog(null,
                "Would you like to save changes to your portfolio?", "Save On Exit Prompt",
                JOptionPane.YES_NO_CANCEL_OPTION);

        switch (answer) {
            case 0: // Save portfolio and exit
                System.out.println("yes");
                break;
            case 1 : // exit without save
                System.out.println("no");
                break;
            case 2: //exit popup
                System.out.println("cancel");
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + answer);
        }

    }

    // MODIFIES: this
    // EFFECTS:  Changes the background of the frame to color
    private void setBackgroundColor(Color color) {
        this.getContentPane().setBackground(color);
    }


}

