package ui.gui.securities;

import model.Security;
import ui.gui.MenuFrame;
import ui.gui.portfolio.PortfolioNavigatorMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import static ui.gui.securities.TransactionHistogram.MAX_VALUES;

// A menu that displays various data about a held security to a portfolio user
public class SecurityMenu extends MenuFrame {
    public static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.CANADA);
    protected static final int WIDTH = 900;
    private static final int HEIGHT = 640;

    protected PortfolioNavigatorMenu parent;

    Map<String, SecurityMenu> menus;

    Security security;

    TransactionTable listings;
    TransactionHistogram barGraph;
    JPanel lowerPanel;

    JButton addButton;
    JButton removeButton;
    AddButtonListener addButtonListener;
    RemoveButtonListener removeButtonListener;
    // EFFECTS:  Sets up a window to view or modify the transactions for a given security

    public SecurityMenu(PortfolioNavigatorMenu parent, Security security, Map<String, SecurityMenu> openMenus) {
        super(security.getTicker());
        this.parent = parent;
        this.security = security;
        this.menus = openMenus;
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(new GridLayout(2,1));
        setup();
        this.add(listings);
        this.add(lowerPanel);
        setVisible(true);
    }

    private void setup() {
        createListings();
        createButtons();
        makeLowerPanel();
        addButtons();
        addInstructions();
        addHistogram();
    }

    // MODIFIES: this
    // EFFECTS:  Creates a set of transactions to view or select for the user
    private void createListings() {
        listings = new TransactionTable(security);
        listings.setOpaque(true);

    }

    // MODIFIES: this
    // EFFECTS:  Adds a lower button and instruction panel to the window
    private void makeLowerPanel() {
        lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBackground(Color.LIGHT_GRAY);
        lowerPanel.setPreferredSize(new Dimension(200, 100));
    }

    // MODIFIES: this
    // EFFECTS:  Adds a instructions to the window
    private void addInstructions() {
        JTextArea textArea = new JTextArea(getInstructions());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        lowerPanel.add(textArea, BorderLayout.SOUTH);
    }

    // EFFECTS: The text instructions for the user
    private String getInstructions() {
        return "Add and remove transactions for this security. \n Information will update automatically. "
                + "\n Close window to exit.";
    }


    // MODIFIES: this
    // EFFECTS:  Adds a button panel to the frame to manipulate transactions
    private void addButtons() {
        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(removeButton);
        lowerPanel.add(buttons, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS:  Adds a recent transactions bar graph to the bottom panel
    private void addHistogram() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Most Recent " + MAX_VALUES + " Transactions"));
        barGraph = new TransactionHistogram(security);
        panel.add(barGraph);
        lowerPanel.add(panel, BorderLayout.CENTER);
    }


    // MODIFIES: this
    // EFFECTS:  Makes some add and remove buttons for transactions

    private void createButtons() {
        addButton = new JButton("Add Transaction");
        addButtonListener = new AddButtonListener();
        addButton.addActionListener(addButtonListener);

        removeButton = new JButton("Remove Transaction");
        removeButtonListener = new RemoveButtonListener();
        removeButton.addActionListener(removeButtonListener);
    }

    // Handles the add button actions
    private class AddButtonListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS:  creates a new pop-up box that records user input to enter a new transaction

        @Override
        public void actionPerformed(ActionEvent e) {
            openEntryPanel();

            // Re-enable the remove button if it was missing
            if (!removeButton.isEnabled()) {
                removeButton.setEnabled(true);
            }
        }
    }

    private void openEntryPanel() {
        new TransactionEntryPanel(this, security);
    }

    // Handler for the remove button actions
    private class RemoveButtonListener implements ActionListener {

        //REQUIRES: The listings index size and order must match the security history size and order
        //MODIFIES: this
        //EFFECTS: Removes the transaction from security, updates the list

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = listings.getTable().getSelectedRow();
            if (index >= 0) {
                security.removeTransaction(index);
            }
            updateTransactions(index);
            parent.refreshList();
        }
    }
    // MODIFIES: this
    // EFFECTS: Refreshed the transactions list

    protected void updateTransactions(int index) {
        this.remove(listings);
        this.remove(lowerPanel);
        setup();
        this.add(listings);
        this.add(lowerPanel);
        invalidate();
        validate();
        repaint();
        placeSelector(index);
    }
    // MODIFIES: this
    // EFFECTS: Resets the selection bar to an appropriate spot

    private void placeSelector(int index) {
        int size = listings.getModel().getRowCount();

        if (size == 0) {
            removeButton.setEnabled(false);
        } else { //Select an index.
            if (index == size) {
                //removed item in last position
                index--;
            }
            listings.getTable().setRowSelectionInterval(index, index);
        }
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Closes this window and removes itself from menus map, refreshes the portfolio navigator listings
    protected void closePrompt() {
        menus.remove(security.getTicker());
        parent.refreshList();
        this.dispose();
    }

    @Override
    public PortfolioNavigatorMenu getParent() {
        return parent;
    }
}
