package model;

import java.util.ArrayList;

public class Portfolio {

    private String name;
    private ArrayList<Security> holdings = new ArrayList<>();

    public Portfolio(String name) {
        this.name = name;
    }

    // REQUIRES: The security is not a duplicate name
    // MODIFIES: this
    // EFFECTS:  Adds a new security to this with the name matching input
    public void addNewSecurity(Security s) {
        holdings.add(s);
    }

    // REQUIRES: An existing security from holdings
    // MODIFIES: this
    // EFFECTS: this has the security removed and all of its associated transactions
    public void removeSecurity(Security s) {
        holdings.remove(s);
    }

    // REQUIRES: A valid transaction to a matching security from the holdings
    // MODIFIES: this
    // EFFECTS: this has a new transaction added to it
    public void addTransaction(Transaction transAdd) {
        transAdd.getSecurity().addTransaction(transAdd);
    }

    // EFFECTS: Returns a list of all of the securities held in this
    public ArrayList<Security> getHoldings() {
        return holdings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
