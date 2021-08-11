package ui.gui.portfolio;

import exceptions.NoTickerException;
import model.Portfolio;
import model.Security;
import persistence.FileFinder;
import ui.gui.securities.SecurityMenu;
import ui.gui.StringSelectionScrollPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static persistence.FileFinder.JSON_FILE_EXTENSION;
import static persistence.FileFinder.PORTFOLIO_DIRECTORY;
import static ui.console.JCapTrack.DOLLAR_FORMAT;

// This is a menu that handles viewing securities and requesting imports and tax documents
public class PortfolioNavigatorMenu extends StringSelectionScrollPanel {
    private static final String LIST_FORMAT = "%6s | %6d  |  %12s | %2d\n";

    private final Portfolio user;
    protected JPanel additionalButtons;
    private final Map<String, SecurityMenu> openSecurityMenus; // A list of all the open security menus by ticker name
    private final Map<String, PortfolioNavigatorMenu> openPortfolios; // A list of all the currently open portfolios


    // EFFECTS: makes a portfolio navigation menu in the default style of JCapTrackMenu
    public PortfolioNavigatorMenu(Portfolio p, Map<String, PortfolioNavigatorMenu> openPortfolios) {
        super(p.getName());
        user = p;
        openSecurityMenus = new HashMap<>();
        this.openPortfolios = openPortfolios;
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

    // MODIFIES: this
    // EFFECTS: Creates a new header for the scroll table
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
    // EFFECTS: Makes the select button open a new security navigation window
    // and adds the window the list of open menus
    @Override
    protected void selectButtonBehavior() {
        String name = listItemtoTicker(namesList.getSelectedValue());

        if (openSecurityMenus.containsKey(name)) {
            SecurityMenu existing = openSecurityMenus.get(name);
            focusSecurityWindow(existing);
        } else {
            try {
                Security s = user.matchString(name);
                openSecurityMenus.put(name, new SecurityMenu(this, s, openSecurityMenus));
            } catch (NoTickerException e) {
                e.printStackTrace();
            }
        }
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

    private void makeExtraButtons() {
        JButton importButton = new JButton("Import CSV");
        importButton.addActionListener(new CsvImportListener(user, this));
        JButton taxButton = new JButton("Tax Slips");
        taxButton.addActionListener(new TaxSlipListener(user, this));

        additionalButtons = new JPanel();
        additionalButtons.setLayout(new GridLayout(2, 1));
        additionalButtons.add(importButton);
        additionalButtons.add(taxButton);

        additionalButtons.setBounds(SCROLL_PANE_WIDTH, 300, WIDTH - SCROLL_PANE_WIDTH - 10, 150);
        add(additionalButtons);
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

    // EFFECTS: isolates the ticker name from a list line and returns it
    private String listItemtoTicker(String item) {
        String name = item.trim();
        return name.substring(0, name.indexOf(' '));
    }

    // EFFECTS: returns a list formatted to display security info
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

    // EFFECTS: focuses existing window to the front of the screen
    private void focusSecurityWindow(SecurityMenu existing) {
        existing.toFront();
        existing.repaint();
    }

    private String getFilepath() {
        return PORTFOLIO_DIRECTORY + "/" + user.getName() + JSON_FILE_EXTENSION;
    }

    // MODIFIES: this
    // EFFECTS: refreshes all of the displayed data in the frame
    public void refreshList() {
        this.remove(jsp);
        setup();
        invalidate();
        validate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to save changes to their portfolio before exiting and acts accordingly
    @Override
    public void closePrompt() {
        Toolkit.getDefaultToolkit().beep();
        int answer = JOptionPane.showConfirmDialog(null,
                "Would you like to save changes to your portfolio?", "Save On Exit Prompt",
                JOptionPane.YES_NO_CANCEL_OPTION);

        switch (answer) {
            case 0: // Save portfolio and exit
                savePortfolio();
                cleanUpExit();
                break;
            case 1: // exit without save
                cleanUpExit();
                break;

            default:
                // Close the window
        }
    }

    // MODIFIES: data
    // EFFECTS: Saves this portfolio to disk
    private void savePortfolio() {
        try {
            System.out.println("Writing your portfolio to disk...");
            FileFinder.writePortfolioSaveFile(user, getFilepath());
        } catch (Exception e) {
            System.out.println("Unable to save your data!");
            e.printStackTrace();
        }
    }

    // MODIFIES: data
    // EFFECTS: Makes sure that all of the open security windows close properly and then closes this, removes this
    //          from the list of open portfolios
    private void cleanUpExit() {
        for (SecurityMenu securityMenu : openSecurityMenus.values()) {
            securityMenu.dispose();
        }
        openPortfolios.remove(user.getName());
        this.dispose();
    }

    public void enableButtons() {
        this.selectButton.setEnabled(true);
        this.removeButton.setEnabled(true);
    }
}

