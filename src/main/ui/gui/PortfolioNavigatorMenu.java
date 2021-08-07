package ui.gui;

import model.Portfolio;
import model.Security;
import persistence.FileFinder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static persistence.FileFinder.JSON_FILE_EXTENSION;
import static persistence.FileFinder.PORTFOLIO_DIRECTORY;
import static ui.JCapTrack.DOLLAR_FORMAT;

// This is a menu that handles viewing securities and requesting imports and tax documents
public class PortfolioNavigatorMenu extends StringSelectionScrollPanel {
    private static final String LIST_FORMAT = "%6s | %6d  |  %12s | %2d\n";

    private final Portfolio user;
    protected JPanel additionalButtons;


    // EFFECTS: makes a portfolio navigation menu in the default style of JCapTrackMenu
    public PortfolioNavigatorMenu(Portfolio p) {
        super(p.getName());
        user = p;
        setup();
        setupInformationPanel();
        makeExtraButtons();
        setVisible(true);
    }




    // MODIFIES: this
    // EFFECTS: Adds a graphic and instruction panel to the right side of the window
    private void setupInformationPanel() {
        JTextArea textArea = new JTextArea(getInstructions());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setBounds(SCROLL_PANE_WIDTH, 100, WIDTH - SCROLL_PANE_WIDTH - 10, 200);
        add(textArea);
    }

    // EFFECTS: The text instructions for the user
    private String getInstructions() {
        return "Use the menu on the left to navigate through your holdings. \n\n"
                + "You can select a security to add a transaction and to view more details. \n\n"
                + "Click a button below to import an https://www.adjustedcostbase.ca/ csv file. \n\n"
                + "Add a year to the generate tax slips to get the gains for that year";
    }


    @Override
    protected void makeHeader() {
        JLabel header = new JLabel(user.getName() + "'s Holdings");
        header.setFont(new Font("Arial Black", Font.BOLD, 20));
        header.setBounds(0, 0, SCROLL_PANE_WIDTH, HEADER_HEIGHT / 2);

        JLabel details = new JLabel("   Name:      Shares:        ACB:                          Transactions:");
        details.setBounds(0, HEADER_HEIGHT / 2, SCROLL_PANE_WIDTH, HEADER_HEIGHT / 2);
        details.setVerticalAlignment(SwingConstants.BOTTOM);

        add(header);
        add(details);
    }

    // MODIFIES: this
    // EFFECTS: Adds a new security to the user portfolio and updates the display
    @Override
    protected void addButtonBehavior() {
        String name = textBox.getText();

        if (checkName(name)) {
            user.addNewSecurity(name);
            namesModel.insertElementAt(makeListItemString(new Security(name)), 0);

            //Reset the text field.
            textBox.requestFocusInWindow();
            textBox.setText("");

            //Select the new item and make it visible.
            namesList.setSelectedIndex(0);
            namesList.ensureIndexIsVisible(0);
        }
    }

    // MODIFIES: this
    // EFFECTS:  Returns true if a valid name was entered, otherwise false
    //           If the field was blank or the name already is in the list then warn the user
    private boolean checkName(String name) {

        if (name.equals("") || user.hasTicker(name)) {

            textBox.requestFocusInWindow();
            textBox.selectAll();

            String warning;
            if (name.equals("")) {
                warning = "You must enter a name to add security";
            } else {
                warning = "That name is already associated with a security";
            }
            errorMessagePopup(warning);
            return false;
        }
        return true;
    }

    // TODO Security menu implement
    // EFFECTS: Opens a security viewer window for the selected security
    @Override
    protected void selectButtonBehavior() {
        String name = listItemtoTicker(namesList.getSelectedValue());

        System.out.println(name);
    }

    private String listItemtoTicker(String item) {
        String name = item.trim();
        return name.substring(0, name.indexOf(' '));
    }

    // MODIFIES: this
    // EFFECTS: when the remove button is pushed it will delete the security selected
    //          It should prompt the user to continue with a warning message
    @Override
    protected void removeButtonBehavior() {
        if (confirmDelete("security")) {
            String name = listItemtoTicker(namesList.getSelectedValue());
            user.removeSecurity(name);
            removeFromList();
        }
    }


    // EFFECTS: Gets a list formatted to display security info
    @Override
    protected List<String> getNamesString() {
        List<String> summary = new ArrayList<>();

        // Make the summary lines
        for (Security s : user.getHoldings()) {
            summary.add(makeListItemString(s));
        }
        return summary;
    }

    // EFFECTS: converts a security into a list line string for viewing
    private String makeListItemString(Security s) {
        return String.format(LIST_FORMAT, s.getTicker(), s.getShares(),
                DOLLAR_FORMAT.format(s.getAcb()), s.getNumTransactions());
    }

    @Override
    protected void closePrompt() {
        Toolkit.getDefaultToolkit().beep();
        int answer = JOptionPane.showConfirmDialog(null,
                "Would you like to save changes to your portfolio?", "Save On Exit Prompt",
                JOptionPane.YES_NO_CANCEL_OPTION);

        switch (answer) {
            case 0: // Save portfolio and exit
                savePortfolio();
                this.dispose();
                break;
            case 1: // exit without save
                this.dispose();
                break;

            default:
                // Close the window
        }
    }

    private void savePortfolio() {
        try {
            System.out.println("Writing your portfolio to disk...");
            FileFinder.writePortfolioSaveFile(user, getFilepath());
        } catch (Exception e) {
            System.out.println("Unable to save your data!");
            e.printStackTrace();
        }
    }

    private String getFilepath() {
        return PORTFOLIO_DIRECTORY + "/" + user.getName() + JSON_FILE_EXTENSION;
    }

    private void makeExtraButtons() {
        JButton importButton = new JButton("Import CSV");
        importButton.addActionListener(new CsvImportListener(user, this));
        JButton taxButton = new JButton("Tax Slips");
        taxButton.addActionListener(new CsvImportListener(user, this));

        additionalButtons = new JPanel();
        additionalButtons.setLayout(new GridLayout(2,1));
        additionalButtons.add(importButton);
        additionalButtons.add(taxButton);

        additionalButtons.setBounds(SCROLL_PANE_WIDTH, 300, WIDTH - SCROLL_PANE_WIDTH - 10, 150);
        add(additionalButtons);


    }

    private void addImportCsvButton() {
        JButton importButton = new JButton("Import CSV");
        importButton.addActionListener(new CsvImportListener(user, this));
    }

    private void addTaxSlipButton() {
        JButton taxButton = new JButton("Tax Slips");
        taxButton.addActionListener(new CsvImportListener(user, this));
    }


    public void refreshList() {
        new PortfolioNavigatorMenu(user);
        this.dispose();
    }
}

