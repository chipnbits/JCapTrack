package ui.gui;

import model.Security;
import model.Transaction;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

import static ui.gui.SecurityMenu.DOLLAR_FORMAT;

public class TransactionTable extends JPanel {

    private static final String[] COLUMN_NAMES = {"Date",
            "Buy/Sell",
            "Value",
            "Currency",
            "FX Rate",
            "Shares",
            "Commission",
            "Capital Gains",
            "Shares Total",
            "ACB Total"
    };
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Security security;

    JTable table;
    DefaultTableModel model;

    public TransactionTable(Security security) {
        super(new GridLayout(1, 0));
        this.security = security;

        makeModel();
        makeTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    // MODIFIES: this
    // EFFECTS: Builds a table from a model and makes
    private void makeTable() {
        table = new JTable();
        table.setModel(model);
        table.setFont(new Font("Serif", Font.PLAIN, 14));
        setColumnWidths();
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
    }

    // MODIFIES: this
    // EFFECTS: sets table column widths
    private void setColumnWidths() {
        for (int i = 0; i < 10; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates a new table model from the security transaction data
    private void makeModel() {
        model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.setColumnIdentifiers(COLUMN_NAMES);
        for (Transaction t : security.getTransactionList()) {
            model.addRow(convertToArray(t));
        }
    }

    private Object[] convertToArray(Transaction t) {
        Object[] data;
        data = new Object[12];

        data[0] = simpleDateFormat.format(t.getDate().getTime());
        data[1] = getTransType(t.getBuyOrSell());
        data[2] = DOLLAR_FORMAT.format(t.getValue());
        data[3] = getCurrencyType(t.getCurrency());
        data[4] = t.getFxRate();
        data[5] = t.getShares();
        data[6] = t.getCommission();
        data[7] = DOLLAR_FORMAT.format(t.getGains());
        data[8] = t.getNewTotalShares();
        data[9] = DOLLAR_FORMAT.format(t.getNewTotalACB());

        return data;
    }

    private String getTransType(boolean type) {
        if (type) {
            return "Sell";
        } else {
            return "Buy";
        }
    }

    private String getCurrencyType(boolean type) {
        if (type) {
            return "USD";
        } else {
            return "CAD";
        }
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    // MODIFIES: this
    // EFFECTS: Removes the currently selected row.
    public void removeTableRow() {
        int index = table.getSelectedRow();
        model.removeRow(index);
    }

    public void updateModel() {
        model.fireTableDataChanged();
    }


}