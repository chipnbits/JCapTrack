package ui.gui.securities;

import model.Security;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilCalendarModel;
import ui.gui.MenuFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

// A Class to handle the entry and validation of transaction data
public class TransactionEntryPanel extends MenuFrame implements ActionListener {

    private final SecurityMenu parent;
    private final int width = 720;

    private final Security security;
    TransactionDataValidator tdv = new TransactionDataValidator();

    private final JPanel dataPanel = new JPanel();
    private final JPanel buttonPanel = new JPanel();

    private JDatePickerImpl datePicker;
    private final JComboBox<String> buyOrSell = new JComboBox<>();
    private final JComboBox<String> currency = new JComboBox<>();

    private final JTextField valueDollar = new JTextField(10);
    private final JTextField valueCents = new JTextField(2);
    private final JTextField sharesText = new JTextField(10);
    private final JTextField comDollar = new JTextField(5);
    private final JTextField comCents = new JTextField(2);
    private final JTextField fxDollar = new JTextField(6);

    private JButton addTrans;
    private JButton cancelButton;

    // EFFECTS: Builds a transaction entry UI Frame
    public TransactionEntryPanel(SecurityMenu parent, Security security) {
        super(security.getTicker() + " Transaction Entry Window");
        this.parent = parent;
        this.setSize(width, HEIGHT / 2);
        this.security = security;

        initFields();
        initButtons();
        dataPanelLayout();
        buttonPanelLayout();
        this.setLayout(new BorderLayout());
        this.add(dataPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds frame buttons and assigns listeners
    private void initButtons() {
        addTrans = new JButton("Add Transaction");
        cancelButton = new JButton("Cancel");
        addTrans.addActionListener(this);
        cancelButton.addActionListener(this);
    }


    // MODIFIES: this
    // EFFECTS: Adds all of the entry components in a grid layout to enter in a security transaction.
    private void dataPanelLayout() {
        dataPanel.setLayout(new GridLayout(0, 3));
        addRowOne();
        addRowTwo();
        addRowThree();
        dataPanel.setBounds(0, 0, width - 20, 100);
    }

    private void addRowOne() {
        dataPanel.add(new JLabel("Select a Date:"));
        dataPanel.add(datePicker);
        blankSpot();
    }

    private void addRowTwo() {
        JPanel valPanel = new JPanel();
        valPanel.add(new JLabel("Value: $"));
        valPanel.add(valueDollar);
        valPanel.add(new JLabel("."));
        valPanel.add(valueCents);
        dataPanel.add(valPanel);
        dataPanel.add(buyOrSell);
        dataPanel.add(currency);
    }

    private void addRowThree() {
        JPanel sharesPanel = new JPanel();
        sharesPanel.add(new JLabel("Total Shares:"));
        sharesPanel.add(sharesText);
        dataPanel.add(sharesPanel);
        JPanel comPanel = new JPanel();
        comPanel.add(new JLabel("Commission: $"));
        comPanel.add(comDollar);
        comPanel.add(new JLabel("."));
        comPanel.add(comCents);
        dataPanel.add(comPanel);
        JPanel fxPanel = new JPanel();
        fxPanel.add(new JLabel("USD to CAD rate"));
        fxPanel.add(fxDollar);

        dataPanel.add(fxPanel);
    }

    // MODIFIES: this
    // EFFECTS: adds a blank spot to dataPanel grid
    private void blankSpot() {
        dataPanel.add(new JLabel());
    }

    // MODIFIES: this
    // EFFECTS: Creates the buttonPane with an add transaction and cancel button
    private void buttonPanelLayout() {
        buttonPanel.add(addTrans);
        buttonPanel.add(cancelButton);
    }

    // MODIFIES: this
    // EFFECTS: initialize the non-text fields with dropdown boxes
    private void initFields() {
        datePicker = constructPicker();

        buyOrSell.addItem("Buy");
        buyOrSell.addItem("Sell");

        currency.addItem("CAD");
        currency.addItem("USD");
    }

    // MODIFIES: this
    // EFFECTS: handles button presses.  If add button is pressed verifies data and calls the transaction adder,
    //          otherwise if cancel is pressed it will exit the entry window.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTrans) {
            validateData();
        }

        if (e.getSource() == cancelButton) {
            {
                closePrompt();
            }
        }
    }

    // MODIFIES: this, security
    // EFFECTS: Validates the user input data and notifies them if it is not correct
    //          Returns true if a valid transaction was entered, false otherwise
    private void validateData() {

        Calendar date = (Calendar) datePicker.getModel().getValue();
        boolean isSell = (buyOrSell.getSelectedItem() == "Sell");
        boolean isUSD = (currency.getSelectedItem() == "USD");

        boolean valid = tdv.validateEntries(security, date, isSell, isUSD, valueDollar.getText(), valueCents.getText(),
                sharesText.getText(), comDollar.getText(), comCents.getText(), fxDollar.getText());

        if (valid) {
            tdv.addTransaction(security);
            // Update the security and portfolio data
            parent.updateTransactions(0);
            parent.getParent().refreshList();
            this.dispose();
        } else {
            notifyUserErrors(tdv.getFoundErrors());
        }
    }

    // MODIFIES: this
    // EFFECTS:  Notifies a user of the errors found in data validation
    private void notifyUserErrors(List<String> foundErrors) {
        String message = "The following errors were encountered in the transaction:";

        for (String s : foundErrors) {
            message = message.concat("\n - ").concat(s);
        }
        JOptionPane.showMessageDialog(this, message);
    }


    // MODIFIES: this
    // EFFECTS: closes the window
    @Override
    protected void closePrompt() {
        this.dispose();
    }

    // Some of this code was understood through
    // https://stackoverflow.com/questions/26794698/how-do-i-implement-jdatepicker
    // I needed a good calendar date entry field and sometimes it is better to use an existing library
    // MODIFIES: this
    // EFFECTS: makes a popup calendar to select a date with
    private JDatePickerImpl constructPicker() {
        UtilCalendarModel model = new UtilCalendarModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        Calendar today = Calendar.getInstance();
        model.setDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        model.setSelected(true);

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);

        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

}
