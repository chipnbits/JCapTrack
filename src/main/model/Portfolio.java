package model;

import exceptions.NoTickerException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

import static ui.JCapTrack.DOLLAR_FORMAT;

// A stock portfolio holding information and transactions for various securities
public class Portfolio implements Writable {

    private String name;

    // EFFECTS: Returns an unmodifiable list of all of the security holdings
    public List<Security> getHoldings() {
        return Collections.unmodifiableList(holdings);
    }

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
            return true;
        }
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

        Security transSecurity = matchString(transAdd.getSecurity());
        transSecurity.addTransaction(transAdd);
    }

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
            summary.add(String.format("%6s || %5d   || %10s  ||%2d\n", s.getTicker(), s.getShares(),
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
    public Security matchString(String ticker) throws NoTickerException {

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

    // EFFECTS: Generates a list of all of the tax information for a given year, transactions that had a capital gain
    public List<String> getTaxTransactions(int year) {
        ArrayList<String> transactions = new ArrayList<>();

        for (Security s : holdings) {
            for (Transaction t : s.getHistory()) {
                if (t.getDate().get(Calendar.YEAR) == year && t.getBuyOrSell()) {
                    transactions.add(t.toString());
                }
            }
        }

        return transactions;
    }

    // REQUIRES: A ticker already contained in the holdings
    // EFFECTS: Finds the correct insertion point to maintain alphabetical order of tickers
    private int findInsertionIndex(String ticker) {
        int index = 0;

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
        return index;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("holdings", holdingsToJson());
        json.put("transactions", transactionsToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray holdingsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Security s : holdings) {
            jsonArray.put(s.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: Gathers all transactions in portfolio and returns them as a JSON array
    private JSONArray transactionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Security s : holdings) {
            for (Transaction t : s.getHistory()) {
                jsonArray.put(t.toJson());
            }
        }
        return jsonArray;
    }
}
