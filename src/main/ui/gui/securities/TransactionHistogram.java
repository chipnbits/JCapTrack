package ui.gui.securities;

import model.Security;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static ui.gui.securities.SecurityMenu.DOLLAR_FORMAT;

public class TransactionHistogram extends JPanel {

    public static final int MAX_VALUES = 8;

    private List<Double> tradeValues = new LinkedList<>(); // A list of maximum 8 Trade Values

    // EFFECTS: Imports the required data to make a histogram
    public TransactionHistogram(Security s) {
        getTradeHistory(s);
    }

    // MODIFIES: this
    // EFFECTS:  Adds a new trade value to end of the list, if the list is at max size it will remove the
    // first value and add a new value to the end of list.
    public void addTradeValue(double value) {
        if (tradeValues.size() >= MAX_VALUES) {
            tradeValues.remove(0);
        }
        tradeValues.add(value);
        repaint();
    }

    // MODIFIES: this
    // EFFECTS:  Adds all of the transactions from a security to the histogram, starting with earliest ones.
    private void getTradeHistory(Security s) {
        for (Transaction t : s.getTransactionList()) {
            double value = t.getValue();
            if (t.getBuyOrSell()) {
                value = -1 * value;
            }
            addTradeValue(value);
        }
    }

    // MODIFIES: this
    // EFFECTS: Draws all of the data bars and fills in any empty data fields with blank bars until reaching max values
    //          Adds strings to show the values for each bar below it.
    @Override
    protected void paintComponent(Graphics g) {
        int maxVal = (int) getMaxValue();
        int width = (getWidth() / MAX_VALUES) - 2;

        if (maxVal != 0) {
            int bars = 0;
            int x = 1;
            int panelHeight = getHeight() - 20;
            for (double val : tradeValues) {
                drawBar((int) val, x, maxVal, panelHeight, width, g);

                x += (width + 2);
                bars++;
            }
            while (bars < MAX_VALUES) {
                g.drawRect(x, (panelHeight / 2), width, 0);
                x += (width + 2);
                bars++;
            }
        }
        addDataStrings(g);
    }

    // MODIFIES: this
    // EFFECTS: Draws a data bar onto the panel according to the value given
    private void drawBar(int val, int x, int maxVal, int panelHeight, int width, Graphics g) {
        Color color = val < 0 ? Color.RED : Color.GREEN;
        int height = (int) ((panelHeight - 5) * ((double) val / maxVal)) / 2;
        g.setColor(color);
        g.fillRect(x, panelHeight / 2, width, -height);
        g.setColor(Color.black);
        if (val < 0) {
            g.drawRect(x, panelHeight / 2, width, -height);
        } else {
            g.drawRect(x, (panelHeight / 2) - height, width, height);
        }
    }

    // MODIFIES: this
    // EFFECTS: Fill in the trade values for the bar graph
    private void addDataStrings(Graphics g) {
        int width = (getWidth() / MAX_VALUES);
        g.setColor(new Color(14, 13, 13));
        int x = 0;
        for (double val : tradeValues) {
            g.drawString(DOLLAR_FORMAT.format(val), (x * width) + 10, getHeight() - 10);
            x++;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 150);
    }

    // EFFECTS: Finds the maximum absolute value for a trade from trade history and returns that value
    private double getMaxValue() {
        double ymax = 0;
        for (double val : tradeValues) {
            if (val < 0) {
                val = -val;
            }
            if (val > ymax) {
                ymax = val;
            }
        }
        return ymax;
    }


}
