package ui.gui.jcaptrack;

import exceptions.DirectoryNotFoundException;
import model.Portfolio;
import persistence.FileFinder;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.gui.PortfolioNavigatorMenu;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// A panel that allows for the selection and manipulation of portfolios stored on the computer
// This class is loosely based off the code example ListDemo
// found https://docs.oracle.com/javase/tutorial/uiswing/components/list.html

public class PortfolioSelectionPanel extends JPanel implements ListSelectionListener {
    private static final String IMPORT_DIRECTORY = "./data/portfolios";
    private static final String DATA_FILE_EXTENSION = ".json";

    protected JList<String> namesList;
    protected DefaultListModel<String> namesModel;

    private JButton selectButton;
    private JButton addButton;
    private JButton removeButton;

    private AddButtonListener addButtonListener;
    private SelectButtonListener selectButtonListener;
    private RemoveButtonListener removeButtonListener;

    private JScrollPane scrollPane;
    private JTextField textBox;


    public PortfolioSelectionPanel() {
        super(new BorderLayout());
        getNamesString();
        getNamesModel();
        makeScrollPane();
        buttonSetup();
        textFieldSetup();
        makeButtonPane();
    }

    // MODIFIES: this
    // EFFECTS:  Parses the names contained in the json import directory, if there is a directory read error,
    //           advises user and continues with an empty list of names.
    private void buttonSetup() {
        selectButtonSetup();
        addButtonSetup();
        removeButtonSetup();
    }

    private void selectButtonSetup() {
        selectButton = new JButton("Select");
        selectButtonListener = new SelectButtonListener();
        selectButton.addActionListener(selectButtonListener);
    }

    private void addButtonSetup() {
        addButton = new JButton("Add");
        addButtonListener = new AddButtonListener();
        addButton.addActionListener(addButtonListener);
//Listener setup here
    }

    private void removeButtonSetup() {
        removeButton = new JButton("Remove");
//Listener setup here
    }

    private void makeScrollPane() {
        namesList = new JList(namesModel);
        namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        namesList.setSelectedIndex(0);
        namesList.addListSelectionListener(this);
        namesList.setVisibleRowCount(10);
        scrollPane = new JScrollPane(namesList);
        add(new JScrollPane(namesList), BorderLayout.CENTER);
    }

    private void textFieldSetup() {
        textBox = new JTextField(10);
        textBox.addActionListener(addButtonListener);
        textBox.addActionListener(selectButtonListener);
        textBox.getDocument().addDocumentListener(addButtonListener);
    }

    private void makeButtonPane() {
        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(selectButton);
        buttonPane.add(textBox);
        buttonPane.add(addButton);
        buttonPane.add(removeButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(buttonPane, BorderLayout.PAGE_END);
    }


    // MODIFIES: this
    // EFFECTS:  fills the names model with the list of portfolio names found in the save directory
    private void getNamesModel() {
        namesModel = new DefaultListModel();

        for (String name : getNamesString()) {
            namesModel.addElement(name);
        }
    }



    private List<String> getNamesString() {
        List<String> names = new ArrayList<>();
        try {
            names = FileFinder.getNamesFromSystem(IMPORT_DIRECTORY, DATA_FILE_EXTENSION);
        } catch (DirectoryNotFoundException e) {
            errorMessagePopup("Unable to locate saved portfolio directory");
            names = new ArrayList<>();
        }
        return names;
    }

    // Handles mouse clicks on the select button
    class AddButtonListener implements ActionListener, DocumentListener {

        // REQUIRES: a valid selection is enabled on the list
        // MODIFIES: this
        // EFFECTS:  Loads and opens a saved portfolio from its json file and launches a navigation menu
        public void actionPerformed(ActionEvent e) {
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

        // MODIFIES: save data
        // EFFECTS: allocates a new portfolio save location based on the portfolio name
        private void addPortfolio(Portfolio p) {
            String filepath = getFileLocation(p.getName());

            try {
                JsonWriter writer = new JsonWriter(filepath);
                writer.open();
                writer.write(p);
                writer.close();
            } catch (IOException e) {
                System.out.println("Unable to create a new file location for that portfolio");
            }
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.

        protected boolean alreadyInList(String name) {
            for (int i = 0; i < namesModel.size(); i++) {
                if (namesModel.elementAt(i).toString().equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;

        }

        /**
         * Gives notification that there was an insert into the document.  The
         * range given by the DocumentEvent bounds the freshly inserted region.
         *
         * @param e the document event
         */
        @Override
        public void insertUpdate(DocumentEvent e) {

        }

        /**
         * Gives notification that a portion of the document has been
         * removed.  The range is given in terms of what the view last
         * saw (that is, before updating sticky positions).
         *
         * @param e the document event
         */
        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        /**
         * Gives notification that an attribute or set of attributes changed.
         *
         * @param e the document event
         */
        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }

    // Handles mouse clicks on the select button
    class SelectButtonListener implements ActionListener {

        // REQUIRES: a valid selection is enabled on the list
        // MODIFIES: this
        // EFFECTS:  Loads and opens a saved portfolio from its json file and launches a navigation menu
        public void actionPerformed(ActionEvent e) {
            String name = namesList.getSelectedValue().toString();
            name = getFileLocation(name);

            JsonReader reader = new JsonReader(name);
            try {
                new PortfolioNavigatorMenu(reader.readPortfolio());
            } catch (IOException ioException) {
                errorMessagePopup("Unable to open that selected file due to IOException");
            }
        }
    }



    // Handles mouse clicks on the select button
    class RemoveButtonListener implements ActionListener {

        // REQUIRES: a valid selection is enabled on the list
        // MODIFIES: this
        // EFFECTS:  Loads and opens a saved portfolio from its json file and launches a navigation menu
        public void actionPerformed(ActionEvent e) {
            System.out.println("remove");


        }
    }




    // EFFECTS: Shows a warning message that there is an issue accessing the json import directory
    protected String getFileLocation(String name) {
        return IMPORT_DIRECTORY + "/" + name + DATA_FILE_EXTENSION;
    }

    protected void errorMessagePopup(String message) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showConfirmDialog(topFrame,
                message, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (namesList.getSelectedIndex() == -1) {
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
