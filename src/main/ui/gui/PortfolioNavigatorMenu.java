package ui.gui;

import model.Portfolio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PortfolioNavigatorMenu extends StringSelectionScrollPanel {
    private static final String IMPORT_DIRECTORY = "./data/csv";
    private static final String DATA_FILE_EXTENSION = ".csv";

    private final Portfolio user;

    // EFFECTS: makes a new frame in the default style of JCapTrackMenu
    public PortfolioNavigatorMenu(Portfolio p) {
        super(p.getName());
        user = p;

    }


    // MODIFIES: this
    // EFFECTS:  Changes the background of the frame to color

    private void setBackgroundColor(Color color) {
        this.getContentPane().setBackground(color);
    }

    @Override
    protected void scrollPaneLayout() {

    }

    @Override
    protected void buttonPaneLayout() {

    }

    @Override
    protected void addButtonBehavior() {

    }

    @Override
    protected void selectButtonBehavior() {

    }

    @Override
    protected void removeButtonBehavior() {

    }

    @Override
    protected List<String> getNamesString() {
        return null;
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
            case 1: // exit without save
                this.dispose();
                break;

            default:
                // Close the window
        }
    }
}

