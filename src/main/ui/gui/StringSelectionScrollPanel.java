package ui.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// A panel that allows for the selection and manipulation of portfolios stored on the computer
// This class is loosely based off the code example ListDemo
// found https://docs.oracle.com/javase/tutorial/uiswing/components/list.html

public abstract class StringSelectionScrollPanel extends MenuFrame implements ListSelectionListener {
    protected static final int HEADER_HEIGHT = 100;
    protected static final int SCROLL_PANE_HEIGHT = 550 - HEADER_HEIGHT;
    protected static final int SCROLL_PANE_WIDTH = 340;

    protected JList<String> namesList;
    protected DefaultListModel<String> namesModel;

    protected JButton selectButton;
    protected JButton addButton;
    protected JButton removeButton;

    protected JPanel buttonPane;

    protected AddButtonListener addButtonListener;
    protected SelectButtonListener selectButtonListener;
    protected RemoveButtonListener removeButtonListener;

    protected JTextField textBox;


    public StringSelectionScrollPanel(String name) {
        super(name);
    }

    protected void setup() {
        getNamesString();
        getNamesModel();
        makeHeader();
        makeScrollPane();
        buttonSetup();
        textFieldSetup();
        makeButtonPane();

    }

    protected abstract void makeHeader();

    // MODIFIES: this
    // EFFECTS:  Parses the names contained in the json import directory, if there is a directory read error,
    //           advises user and continues with an empty list of names.
    private void buttonSetup() {
        selectButtonSetup();
        addButtonSetup();
        removeButtonSetup();
        if (namesModel.size() <= 0) {
            //No items in list disable buttons
            selectButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
    }

    private void selectButtonSetup() {
        selectButton = new JButton("Select");
        selectButtonListener = new SelectButtonListener();
        selectButton.addActionListener(selectButtonListener);
    }

    private void addButtonSetup() {
        addButton = new JButton("Add");
        addButtonListener = new AddButtonListener(addButton);
        addButton.addActionListener(addButtonListener);
    }

    private void removeButtonSetup() {
        removeButton = new JButton("Remove");
        removeButtonListener = new RemoveButtonListener();
        removeButton.addActionListener(removeButtonListener);
    }

    // MODIFIES: this
    // EFFECTS: Builds a new scroll pane to view and select a list of strings
    protected void makeScrollPane() {
        namesList = new JList<>(namesModel);
        namesList.setFont(new Font("Consolas", Font.PLAIN, 12));
        namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        namesList.setSelectedIndex(0);
        namesList.addListSelectionListener(this);
        namesList.setVisibleRowCount(10);
        scrollPaneLayout();
    }

    // MODIFIES: this
    // EFFECTS: Choose where to layout the scroll pane
    protected void scrollPaneLayout() {
        JScrollPane jsp = new JScrollPane(namesList);
        jsp.setBounds(0, HEADER_HEIGHT, SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT);
        this.add(jsp);
    }


    // MODIFIES: this
    // EFFECTS: makes a text entry box
    private void textFieldSetup() {
        textBox = new JTextField(10);
        textBox.addActionListener(addButtonListener);
        textBox.addActionListener(selectButtonListener);
        textBox.getDocument().addDocumentListener(addButtonListener);
    }

    // MODIFIES: this
    // EFFECTS: makes the lower area of the pane with the button layout
    private void makeButtonPane() {
        //Create a panel that uses BoxLayout.
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(selectButton);
        buttonPane.add(textBox);
        buttonPane.add(addButton);
        buttonPane.add(removeButton);
        buttonPane.setBorder(BorderFactory.createLineBorder(
                new Color(22, 42, 245, 102), 3, true));
        buttonPaneLayout();
    }

    // MODIFIES: this
    // EFFECTS: Choose where to layout the button pane
    protected void buttonPaneLayout() {
        buttonPane.setBounds(0, SCROLL_PANE_HEIGHT + HEADER_HEIGHT, SCROLL_PANE_WIDTH, 50);
        add(buttonPane);
    }


    // MODIFIES: this
    // EFFECTS:  fills the names model with the list of portfolio names found in the save directory
    protected void getNamesModel() {
        namesModel = new DefaultListModel<>();

        for (String name : getNamesString()) {
            namesModel.addElement(name);
        }


    }

    // EFFECTS: controls the actions performed by clicking the add button
    protected abstract void addButtonBehavior();

    // EFFECTS: controls the actions performed by clicking the select button
    protected abstract void selectButtonBehavior();

    // EFFECTS: controls the actions performed by clicking the remove button
    protected abstract void removeButtonBehavior();

    // EFFECTS: Creates a warning popup to check if user wants to delete a type
    protected boolean confirmDelete(String type) {

        int choice = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete this "
                        + type + "?", "Delete All Data",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        return (choice == JOptionPane.YES_OPTION);
    }

    // MODIFIES: this
    // EFFECTS:  removes the selected item from the list and updates the selection
    protected void removeFromList() {
        int index = namesList.getSelectedIndex();

        namesModel.remove(index);

        // Check to see if button is disabled if empty
        int size = namesModel.getSize();

        if (size == 0) { //Nobody's left, disable firing.
            removeButton.setEnabled(false);

        } else { //Select an index.
            if (index == namesModel.getSize()) {
                //removed item in last position
                index--;
            }
            namesList.setSelectedIndex(index);
            namesList.ensureIndexIsVisible(index);
        }
    }


    // EFFECTS: get a list of strings to populate a scroll list with
    protected abstract List<String> getNamesString();

    // Handles mouse clicks on the add button
    class AddButtonListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private final JButton button;

        public AddButtonListener(JButton button) {
            this.button = button;
        }

        // MODIFIES: this
        // EFFECTS:  Loads and opens a saved portfolio from its json file and launches a navigation menu
        public void actionPerformed(ActionEvent e) {
            addButtonBehavior();
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }


    // Handles mouse clicks on the select button
    class SelectButtonListener implements ActionListener {

        // EFFECTS: performs an action when select button is clicked
        public void actionPerformed(ActionEvent e) {
            selectButtonBehavior();
        }
    }


    // Handles mouse clicks on the remove button
    class RemoveButtonListener implements ActionListener {

        // REQUIRES: a valid selection is enabled on the list
        // MODIFIES: this
        // EFFECTS:  removes an items
        public void actionPerformed(ActionEvent e) {
            removeButtonBehavior();
            int size = namesModel.getSize();

            if (size == 0) { //No items left to remove disable button
                removeButton.setEnabled(false);


            }
        }
    }

    // EFFECTS: makes an error message popup
    protected void errorMessagePopup(String message) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            addButton.setEnabled(true);

            if (namesList.getSelectedIndex() == -1 || namesModel.size() <= 0) {
                //No selection, disable remove and select button
                selectButton.setEnabled(false);
                removeButton.setEnabled(false);


            } else {
                //Selection, enable remove and select buttons
                selectButton.setEnabled(true);
                removeButton.setEnabled(true);
            }
        }
    }
}