package ui.gui;

import model.Security;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class SecurityMenu extends MenuFrame {
    public static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.CANADA);
    private static final int WIDTH = 900;
    private static final int HEIGHT = 640;

    Map<String, SecurityMenu> menus;
    Security security;

    TransactionTable listings;

    JButton addButton;
    JButton removeButton;
    AddButtonListener addButtonListener;
    RemoveButtonListener removeButtonListener;

    JPanel buttonPanel;

    // EFFECTS:  Sets up a window to view or modify the transactions for a given security
    public SecurityMenu(Security security, Map<String, SecurityMenu> openMenus) {
        super(security.getTicker());
        this.security = security;
        this.menus = openMenus;
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(new BorderLayout());
        createListings();
        createButtons();
        addButtonPanel();

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS:  Adds a button panel to the frame to manipulate transactions
    private void addButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setSize(new Dimension(WIDTH, 200));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
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

    // MODIFIES: this
    // EFFECTS:  Creates a set of transactions to view or select for the user
    private void createListings() {
        listings = new TransactionTable(security);
        listings.setOpaque(true);
        listings.setMaximumSize(new Dimension(WIDTH, HEIGHT / 2));
        this.add(listings, BorderLayout.NORTH);
    }

    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            TransactionEntryPanel entry = new TransactionEntryPanel();
        }
    }

    // Handler for the remove button actions
    private class RemoveButtonListener implements ActionListener {

        //REQUIRES: The listings index size and order must match the security history size and order
        //MODIFIES: this
        //EFFECTS: Removes the transaction from security, updates the list
        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTableModel model = listings.getModel();
            int index = listings.getTable().getSelectedRow();
            int size = model.getRowCount() - 1; // One item is removed

            if (index >= 0) {
                security.removeTransaction(index);

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
            updateTransactions();
        }
    }

    // MODIFIES: this
    // EFFECTS: Refreshed the transactions list
    private void updateTransactions() {
        this.remove(listings);
        createListings();
        invalidate();
        validate();
        repaint();
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Closes this window and removes itself from menus map
    protected void closePrompt() {
        menus.remove(security.getTicker());
        this.dispose();
    }
}
