package model;

import java.util.ArrayList;

import static ui.JCapTrack.DOLLAR_FORMAT;

// Model of a security that is traded on a stock exchange
public class Security {

    private String ticker;     // Security ticker symbol
    private String name;       // Name of company
    private int shares;        // Current number of shares held
    private double acb;        // Current adjusted cost base for the shares
    private ArrayList<Transaction> history = new ArrayList<>();  // A list ordered by date of the trading history

    public Security(String ticker) {
        this.ticker = ticker;
    }

    // Option to add a name to the security
    public Security(String ticker, String name) {
        this.ticker = ticker;
        this.name = name;
    }


    // REQUIRES: A transaction matching the security
    // MODIFIES: this, transAdd
    // EFFECTS: Adds a new transaction and updates the information of the security traded
    //          Updates all of the capital gains and acb details of any transactions that
    //          took place after the added transaction.  (past dated transactions allowed)
    public void addTransaction(Transaction transAdd) {
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
        // Update all the transactions after
        while (index < history.size()) {
            Transaction prev = history.get(index - 1);
            history.get(index).updateTransaction(prev.getNewTotalShares(), prev.getNewTotalACB());
            index++;
        }
        // Update the security information
        Transaction last = history.get(history.size() - 1);
        this.shares = last.getNewTotalShares();
        this.acb = last.getNewTotalACB();
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

    @Override
    public String toString() {
        return String.format("Name: " + ticker + " Shares: " + shares + " ACB: "
                + DOLLAR_FORMAT.format(acb) + " Transactions: " + history.size());
    }
}

