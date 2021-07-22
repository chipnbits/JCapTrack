package model;


import java.util.Calendar;

import static ui.JCapTrack.DOLLAR_FORMAT;

// Represents a recorded transaction for a security including all of the relevant information at the time it occurs
public class Transaction {

    private final String ticker;        // Name of Security
    private final Calendar date;        // Date of transaction
    private final boolean isSell;       // true for Sell

    private final double value;         // Trade value in CAD or USD
    private final boolean isUSD;        // true for USD

    private final double fxRate;        // The USD to CAD exchange rate on the date of transaction
    private final int shares;           // The number of shares exchanged
    private final double commission;    // The commission charged by brokerage
    private double gains;         // Any capital gains or losses incurred *Always in CAD*
    private int newTotalShares;   // The new total for shares of the security after this transaction
    private double newTotalACB;      // The new ACB for the security after this transaction *always in CAD*

    // REQUIRES: - type matches an index from the TYPE_OF_TRANSACTION array
    //           - date is today or earlier
    //           - val >= 0
    //           - shares > 0
    //           - commission >= 0
    // EFFECTS: Makes a new transaction
    public Transaction(String ticker, Calendar date, boolean type, double val,
                       boolean fx, double rate, int shares, double commission) {
        this.date = date;
        isSell = type;
        this.ticker = ticker;
        value = val;
        isUSD = fx;
        fxRate = rate;
        this.shares = shares;
        this.commission = commission;
    }

    // REQUIRES: acb values are always in CAD
    // MODIFIES: this
    // EFFECTS:  Updates the capital gains and total shares/acb fields based on past transaction history
    public void updateTransaction(int prevShares, double prevACB) {
        double val = value;
        double com = commission;
        if (isUSD) {
            val *= fxRate;  // CAD conversion
            com *= fxRate;  // CAD conversion
        }
        if (isSell) {
            gains = val - com - (prevACB / prevShares) * this.shares;  // CAD
            newTotalShares = prevShares - this.shares;
            newTotalACB = (prevACB * (prevShares - shares)) / prevShares; // CAD
        } else {
            gains = 0; // This is essentially null, there are no gains for a buy trade
            newTotalShares = prevShares + this.shares;
            newTotalACB = prevACB + val + com;  // The adjusted cost base has the cost of shares and commission added
        }
    }

    public Calendar getDate() {
        return date;
    }

    public boolean getBuyOrSell() {
        return isSell;
    }

    public String getSecurity() {
        return ticker;
    }

    public double getValue() {
        return value;
    }

    public boolean getCurrency() {
        return isUSD;
    }

    public double getFxRate() {
        return fxRate;
    }

    public int getShares() {
        return shares;
    }

    public double getCommission() {
        return commission;
    }

    public double getGains() {
        return gains;
    }

    public int getNewTotalShares() {
        return newTotalShares;
    }

    public double getNewTotalACB() {
        return newTotalACB;
    }

    @Override
    // EFFECTS: Produces a string output of all of the transaction information
    //          -Date
    //          -Details
    //          -Value
    //          -Commission
    public String toString() {
        String currency = "CAD";
        String type = "Buy";
        if (isUSD) {
            currency = "USD";
        }
        if (isSell) {
            type = "Sell";
        }
        return String.format("Date: %1$tY-%1$tB-%1$td\n"
                + type + " " + shares + " shares of " + ticker + "\n"
                + "Value: " + DOLLAR_FORMAT.format(value) + " " + currency + "\n"
                + "Commission: " + DOLLAR_FORMAT.format(commission) + " " + currency
                + "\nGains: " +  DOLLAR_FORMAT.format(gains)
                + "\nTotalShares: " + newTotalShares
                + "\nACB: " + DOLLAR_FORMAT.format(getNewTotalACB()) + " CAD", date);
    }
}
