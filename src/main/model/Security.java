package model;

import java.util.ArrayList;
import java.util.List;

import static ui.JCapTrack.DOLLAR_FORMAT;

// Model of a security that is traded on a stock exchange
class Security {

    private final String ticker;     // Security ticker symbol
    private String name;       // Name of company
    private int shares;        // Current number of shares held
    private double acb;        // Current adjusted cost base for the shares
    private final List<Transaction> history;  // A trading history for the security ordered by date

    protected Security(String ticker) {
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
        // If it is the first transaction in the history then parameters are zero
        if (index == 0) {
            transAdd.updateTransaction(0, 0);
            index++;
        }
        // Update the security information
        updateSecurity(index);
    }

    // REQUIRES: A valid index that is within the history list size 0 <= index <= history size
    // MODIFIES: this
    // EFFECTS: Updates history details and the new shares and acb balance for this
    private void updateSecurity(int index) {
        updateSecurityHistory(index);
        Transaction last = history.get(history.size() - 1);
        this.shares = last.getNewTotalShares();
        this.acb = last.getNewTotalACB();

    }

    // REQUIRES: A valid index that is within the history list size 0 <= index <= history size
    // MODIFIES: this
    // EFFECTS: Updates history details starting at the given index
    private void updateSecurityHistory(int index) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // EFFECTS: returns a string with the basic details of this, name, shares, acb, and number of transactions
    @Override
    public String toString() {
        return ("Name: " + ticker + " Shares: " + shares + " ACB: "
                + DOLLAR_FORMAT.format(acb) + " Transactions: " + history.size());
    }
}

