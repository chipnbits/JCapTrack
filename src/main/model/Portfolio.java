package model;

import exceptions.NoTickerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static ui.JCapTrack.DOLLAR_FORMAT;

// A stock portfolio holding information and transactions for various securities
public class Portfolio {

    private String name;
    private List<Security> holdings;  // A list of all the securities held, with no duplicates

    public Portfolio(String name) {
        this.name = name;
        holdings = new ArrayList<>();
    }


    // MODIFIES: this
    // EFFECTS:  Tries to add a new security to this with the name matching input, returns true if successful
    //           returns false if the security ticker is already listed
    public boolean addNewSecurity(String ticker) {
        if (!getTickers().contains(ticker)) {
            holdings.add(new Security(ticker));
            return true;
        }
        return false;
    }


    // MODIFIES: this
    // EFFECTS: returns true if security is found in portfolio and removes it
    //          if the security is not in the portfolio, return false
    public boolean removeSecurity(String ticker) {
        for (Security s : holdings) {
            if (s.getTicker().equals(ticker)) {
                holdings.remove(s);
                return true;
            }
        }
        return false;
    }

    // TODO implement exception for requires clause
    // REQUIRES: A valid transaction to a matching security from the holdings
    // MODIFIES: this
    // EFFECTS: returns true if able to add transaction to an existing ticker, adds it to that holding
    //          returns false if unable to find an existing ticker matching transaction
    public boolean addTransaction(Transaction transAdd) {
        String ticker = transAdd.getSecurity();

        for (Security s : holdings) {
            if (ticker.equals(s.getTicker())) {
                s.addTransaction(transAdd);
                return true;
            }
        }
        return false;
    }

//    // Functionality removed to fully encapsulate the class
//    // EFFECTS: Returns a list of the ticker symbol of all of the securities held in this
//    public List<Security> getHoldings() {
//        return holdings;
//    }


    // EFFECTS: returns a list of the names of all of the tickers held in this portfolio in alphabetical order
    public List<String> getTickers() {
        List<String> tickers = new ArrayList<>();

        sortHoldingsTickers();

        for (Security s : holdings) {
            tickers.add(s.getTicker());
        }
        return tickers;
    }

    // EFFECTS: Returns true if the portfolio contains a security matching the given ticker string, false otherwise
    public boolean hasTicker(String ticker) {
        for (Security s : holdings) {
            if (s.getTicker().equals(ticker)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: Returns the number of holdings contained in this
    public int getNumHoldings() {
        return holdings.size();
    }

    // MODIFIES: this
    // EFFECTS: Sorts all of the holdings into alphabetical order, then returns a list of strings
    // containing a full summary of all the account holdings in alphabetical order
    public List<String> getSummary() {
        List<String> summary = new ArrayList<>();

        // Sort the holdings first
        sortHoldingsTickers();

        // Make the summary lines
        for (Security s : holdings) {
            summary.add(String.format("%5s || %5d   || %10s  ||%2d\n", s.getTicker(), s.getShares(),
                    DOLLAR_FORMAT.format(s.getAcb()), s.getNumTransactions()));
        }
        return summary;
    }


    // MODIFIES: this
    // EFFECTS: A brute force system to reorganize holdings in alphabetical order sorted by ticker name
    private void sortHoldingsTickers() {
        ArrayList<Security> ordered = new ArrayList<>();

        for (Security s : holdings) {
            // Get the name of the security
            String ticker1 = s.getTicker().toLowerCase(Locale.ROOT);

            // Add to empty list case
            if (ordered.size() == 0) {
                ordered.add(s);
            } else {
                // Search for the proper index
                int index = 0;
                for (Security orderedSecurity : ordered) {
                    String ticker2 = orderedSecurity.getTicker().toLowerCase(Locale.ROOT);

                    if (ticker1.compareTo(ticker2) >= 0) {
                        index++;
                    } else {
                        break;
                    }
                }
                // Proper index found, add it there
                ordered.add(index, s);
            }
        }
        holdings = ordered;
    }


    // EFFECTS: Searches for all transactions for the security matching the parameter ticker, then returns them
    //          as a list of strings.
    public List<String> searchTransactions(String ticker) throws NoTickerException {
        Security lookUp = matchString(ticker);
        return lookUp.getTransactionRecord();
    }

    // EFFECTS: Returns the security that matches the name of the string given.  Throws an exception if not found
    private Security matchString(String ticker) throws NoTickerException {

        for (Security s : holdings) {
            if (s.getTicker().equals(ticker)) {
                return s;
            }
        }
        throw new NoTickerException("No security found that matches " + ticker);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
