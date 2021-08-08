package ui.gui;

import exceptions.DirectoryNotFoundException;
import model.Portfolio;
import persistence.FileFinder;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Implementation of a string selection panel to choose a portfolio to open
public class PortfolioSelectionPanel extends StringSelectionScrollPanel {
    private static final String WELCOME_IMAGE_LOCATION = "./data/images/tickers.jpg";


    // EFFECTS: Makes a portfolio selection menu with instructions and graphics
    public PortfolioSelectionPanel() {
        super("Portfolio Selection");
        setup();
        setupInformationPanel();
        namesList.setFont(new Font("SansSerif", Font.BOLD, 20));
        setVisible(true);

    }

    // MODIFIES: this
    // EFFECTS: Adds a graphic and instruction panel to the right side of the window
    private void setupInformationPanel() {
        ImageIcon imgIcon = new ImageIcon(WELCOME_IMAGE_LOCATION);

        JLabel img = new JLabel();
        img.setIcon(imgIcon);
        img.setBounds(SCROLL_PANE_WIDTH, 200, WIDTH - SCROLL_PANE_WIDTH, 300);
        add(img);

        JTextArea textArea = new JTextArea(getInstructions());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setBounds(SCROLL_PANE_WIDTH, 100, WIDTH - SCROLL_PANE_WIDTH, 100);
        add(textArea);
    }

    // EFFECTS: The text instructions for the user
    private String getInstructions() {
        return "Hello and welcome to JCapTrack.\n Please select a portfolio."
                + "  You can also add and remove a portfolio by clicking the buttons";
    }


    @Override
    protected void makeHeader() {
        JLabel header = new JLabel("Portfolios:");
        header.setFont(new Font("Arial Black", Font.BOLD, 20));
        header.setBounds(0, 0, SCROLL_PANE_WIDTH, HEADER_HEIGHT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header);
    }

    // MODIFIES: this
    // EFFECTS: retrieves the list of strings destined for display
    @Override
    protected List<String> getNamesString() {
        List<String> names;
        try {
            names = FileFinder.getNamesFromSystem(FileFinder.PORTFOLIO_DIRECTORY, FileFinder.JSON_FILE_EXTENSION);
        } catch (DirectoryNotFoundException e) {
            errorMessagePopup("Unable to locate saved portfolio directory");
            names = new ArrayList<>();
        }
        return names;
    }

    // MODIFIES: this
    // EFFECTS: Makes a new portfolio save file and
    @Override
    protected void addButtonBehavior() {
        String name = textBox.getText();

        if (checkName(name)) {

            addPortfolio(new Portfolio(name));
            namesModel.insertElementAt(name, 0);

            //Reset the text field.
            textBox.requestFocusInWindow();
            textBox.setText("");

            //Select the new item and make it visible.
            namesList.setSelectedIndex(0);
            namesList.ensureIndexIsVisible(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: when the select button is pushed it will attempt to open the file location for that portfolio
    //          and allow navigation of the contents in a new PortfolioNavigator
    @Override
    protected void selectButtonBehavior() {
        String name = namesList.getSelectedValue();
        name = getFileLocation(name);

        JsonReader reader = new JsonReader(name);
        try {
            new PortfolioNavigatorMenu(reader.readPortfolio());
        } catch (IOException ioException) {
            errorMessagePopup("Unable to open that selected file due to IOException");
        }
    }

    // MODIFIES: this
    // EFFECTS: when the remove button is pushed it will attempt to delete the file location for that portfolio,
    //          It should prompt the user to continue with a warning message
    @Override
    protected void removeButtonBehavior() {

        if (confirmDelete("portfolio")) {
            String name = namesList.getSelectedValue();
            removeFromList();
            if (!FileFinder.deleteFile(getFileLocation(name))) {
                errorMessagePopup("Unable to find or delete stored data location!");
            }
        }
    }


    // MODIFIES: this
    // EFFECTS:  Returns true if a valid name was entered, otherwise false
    //           If the field was blank or the name already is in the list then warn the user
    private boolean checkName(String name) {

        if (name.equals("") || alreadyInList(name)) {

            textBox.requestFocusInWindow();
            textBox.selectAll();

            String warning;
            if (name.equals("")) {
                warning = "You must enter a name into the field to add a portfolio";
            } else {
                warning = "That name is already associated with a portfolio";
            }
            errorMessagePopup(warning);
            return false;
        }
        return true;
    }

    //This method tests for string equality. You could certainly
    //get more sophisticated about the algorithm.  For example,
    //you might want to ignore white space and capitalization.
    protected boolean alreadyInList(String name) {
        for (int i = 0; i < namesModel.size(); i++) {
            if (namesModel.elementAt(i).equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: save data
    // EFFECTS: allocates a new portfolio save location based on the portfolio name
    private void addPortfolio(Portfolio p) {
        String filepath = getFileLocation(p.getName());
        FileFinder.writePortfolioSaveFile(p, filepath);
    }

    protected String getFileLocation(String name) {
        return FileFinder.PORTFOLIO_DIRECTORY + "/" + name + FileFinder.JSON_FILE_EXTENSION;
    }


    // MODIFIES: this
    // EFFECTS: Handles the close behavior.  Asks user to confirm the decision to exit program and acts accordingly
    @Override
    protected void closePrompt() {
        Toolkit.getDefaultToolkit().beep();

        int choice = JOptionPane.showConfirmDialog(
                this, "Would you like to exit?", "Quit Program",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }
}





