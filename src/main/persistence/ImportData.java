package persistence;


import model.Portfolio;
import model.Transaction;

import java.util.List;

// Represents a package of security names and transactions to be imported into JCapTrack
public class ImportData {

    private final List<String> securityNames;
    private final List<Transaction> transactions;

    public ImportData(List<String> securityNames, List<Transaction> transactions) {
        this.securityNames = securityNames;
        this.transactions = transactions;
    }

    public List<String> getSecurityNames() {
        return securityNames;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    // MODIFIES: this
    // EFFECTS:  scans the transactions to make sure that they have a matching security name in security names list.
    //           if they don't then create a new ticker name to match the transaction
    private void normalize() {
        for (Transaction t : transactions) {
            if (!securityNames.contains(t.getSecurity())) {
                securityNames.add(t.getSecurity());
            }
        }
    }

    // MODIFIES: this, portfolio
    // EFFECTS: prepares and merges the data from this into the portfolio and updates the portfolio
    public void addToPortfolio(Portfolio portfolio) {
        this.normalize();

        for (String s : this.getSecurityNames()) {
            portfolio.addNewSecurity(s);
        }

        for (Transaction t : this.getTransactions()) {
            portfolio.addTransaction(t);
        }
    }

}
