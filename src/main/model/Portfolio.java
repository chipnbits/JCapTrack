package model;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {

    private String name;
    private List<Security> holdings;  // A list of all the securities held, with no duplicates

    public Portfolio(String name) {
        this.name = name;
        holdings = new ArrayList<>();
    }

    // TODO Modify to take a string instead
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

    // TODO remove this function completely
    // EFFECTS: Returns a list of the ticker symbol of all of the securities held in this
    public List<Security> getHoldings() {
        return holdings;
    }


    // EFFECTS: returns a list of the names of all of the tickers held in this portfolio
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



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
