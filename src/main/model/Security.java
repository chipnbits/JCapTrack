package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ui.JCapTrack.DOLLAR_FORMAT;

// Model of a security that is traded on a stock exchange
public class Security implements Writable {

    private final String ticker;     // Security ticker symbol
    private int shares;        // Current number of shares held
    private double acb;        // Current adjusted cost base for the shares
    private final List<Transaction> history;  // A trading history for the security ordered by date

    public Security(String ticker) {
        this.ticker = ticker;
        this.shares = 0;
        this.acb = 0;
        this.history = new ArrayList<>();

    }

    // REQUIRES: A transaction matching the security ticker
    // MODIFIES: this, transAdd
    // EFFECTS: Adds a new transaction to this and completes the transaction details for transAdd
    //          Updates the trading history and the the holding details of this
    protected void addTransaction(Transaction transAdd) {
        // Start at the beginning of the history
        int index = 0;
        // Find the correct index to insert based on chronological order
        while (index < history.size() && transAdd.getDate().compareTo(history.get(index).getDate()) > 0) {
            index++;
        }
        history.add(index, transAdd);
        // Update the security information
        updateSecurity(index);
    }

    // REQUIRES: the index of the transaction to remove
    // MODIFIES: this
    // EFFECTS: Removes a transaction and updates the trading history and security information
    protected void removeTransaction(int index) {
        history.remove(index);
        // Update the security information
        updateSecurity(index);
    }


    // REQUIRES: A valid index that is within the history list size 0 <= index <= history size
    // MODIFIES: this
    // EFFECTS: Updates history details and the new shares and acb balance for this starting at given index
    private void updateSecurity(int index) {
        if (history.isEmpty()) {
            this.shares = 0;
            this.acb = 0;
        } else {
            updateSecurityHistory(index);
            Transaction last = history.get(history.size() - 1);
            this.shares = last.getNewTotalShares();
            this.acb = last.getNewTotalACB();
        }

    }

    // REQUIRES: A valid index that is within the history list size 0 <= index <= history size
    // MODIFIES: this
    // EFFECTS: Updates history details starting at the given index
    private void updateSecurityHistory(int index) {
        if (index == 0) {
            history.get(index).updateTransaction(0, 0);
            index++;
        }
        while (index < history.size()) {
            Transaction prev = history.get(index - 1);
            history.get(index).updateTransaction(prev.getNewTotalShares(), prev.getNewTotalACB());
            index++;
        }
    }

    // EFFECTS: Returns a record of all the transactions for a security in String form organised by Date
    protected List<String> getTransactionRecord() {
        List<String> output = new ArrayList<>();

        for (Transaction t : history) {
            output.add(t.toString());
        }
        return output;
    }

    public List<Transaction> getTransactionList() {
        return Collections.unmodifiableList(history);
    }

    protected List<Transaction> getHistory() {
        return history;
    }

    public String getTicker() {
        return ticker;
    }

    public double getAcb() {
        return acb;
    }

    public int getShares() {
        return shares;
    }

    public int getNumTransactions() {
        return history.size();
    }


    // EFFECTS: returns a string with the basic details of this, name, shares, acb, and number of transactions
    @Override
    public String toString() {
        return ("Name: " + ticker + " Shares: " + shares + " ACB: "
                + DOLLAR_FORMAT.format(acb) + " Transactions: " + history.size());
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ticker", ticker);
        return json;
    }

}

