package model;

import exceptions.NoTickerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static ui.JCapTrack.DOLLAR_FORMAT;

// A stock portfolio holding information and transactions for various securities
public class Portfolio {

    private String name;
    private List<Security> holdings;  // A list of all the securities held, with no duplicates, in alphabetical order

    public Portfolio(String name) {
        this.name = name;
        holdings = new ArrayList<>();
    }


    // MODIFIES: this
    // EFFECTS:  Tries to add a new security to this with the name matching input, returns true if successful
    //           returns false if the security ticker is already listed
    public boolean addNewSecurity(String ticker) {
        Security s = new Security(ticker);

        if (getTickers().contains(ticker)) {
            return false;
        } else {
            if (holdings.size() == 0) {
                holdings.add(s);
            } else {
                holdings.add(findInsertionIndex(ticker), s);
            }
            assert (checkInvariant());  // Check that securities are ordered
            return true;
        }
    }

    // REQUIRES: A ticker already contained in the holdings
    // EFFECTS: Finds the correct insertion point to maintain alphabetical order of tickers
    private int findInsertionIndex(String ticker) {
        int index = 0;

        if (holdings.size() == 0) {
            index = 0;
        } else {
            // Search for the proper index
            ticker = ticker.toLowerCase(Locale.ROOT);
            for (Security orderedSecurity : holdings) {
                String ticker2 = orderedSecurity.getTicker().toLowerCase(Locale.ROOT);

                if (ticker.compareTo(ticker2) >= 0) {
                    index++;
                } else {
                    break;
                }
            }
        }

        return index;
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

    // MODIFIES: this
    // EFFECTS: if able to add transaction to an existing ticker, adds it to that holding
    //          throws runtime exception if unable to find an existing ticker matching transaction
    public void addTransaction(Transaction transAdd) throws NoTickerException {
        boolean match = false;
        String ticker = transAdd.getSecurity();

        for (Security s : holdings) {
            if (ticker.equals(s.getTicker())) {
                s.addTransaction(transAdd);
                match = true;
            }
        }
        if (!match) {
            throw new NoTickerException("Can't find " + ticker);
        }
        //assert (checkOrdered());
    }

//    // Functionality removed to fully encapsulate the class
//    // EFFECTS: Returns a list of the ticker symbol of all of the securities held in this
//    public List<Security> getHoldings() {
//        return holdings;
//    }


    // EFFECTS: returns a list of the names of all of the tickers held in this portfolio in alphabetical order
    public List<String> getTickers() {
        List<String> tickers = new ArrayList<>();

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

        // Make the summary lines
        for (Security s : holdings) {
            summary.add(String.format("%5s || %5d   || %10s  ||%2d\n", s.getTicker(), s.getShares(),
                    DOLLAR_FORMAT.format(s.getAcb()), s.getNumTransactions()));
        }
        return summary;
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

    // EFFECTS: Checks if the list of securities is in alphabetical order
    //          returns true if they are in order, false otherwise
    private boolean checkInvariant() {
        boolean ordered = true;

        for (int i = 0; i < holdings.size() - 1; i++) {
            if (holdings.get(i).getTicker().compareTo(holdings.get(i + 1).getTicker()) > 0) {
                ordered = false;
                break;
            }
        }
        return ordered;
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
                ordered.add(findInsertionIndex(ticker1), s);
            }
        }
        holdings = ordered;
    }

}
