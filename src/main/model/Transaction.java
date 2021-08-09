package model;


import org.json.JSONObject;
import persistence.Writable;

import java.util.Calendar;

import static ui.console.JCapTrack.DOLLAR_FORMAT;

// Represents a recorded transaction for a security including all of the relevant information at the time it occurs
public class Transaction implements Writable {


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
        this.ticker = ticker;
        this.date = date;
        isSell = type;
        value = val;
        isUSD = fx;
        fxRate = rate;
        this.shares = shares;
        this.commission = commission;
    }

    // REQUIRES: acb values are always in CAD
    // MODIFIES: this
    // EFFECTS:  Updates the capital gains and total shares/acb fields based on past transaction history
    protected void updateTransaction(int prevShares, double prevACB) {
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

    //TODO Add a superficial loss/gain rule if possible
    //A superficial loss can occur when you dispose of capital property for a loss and both of the following conditions
    // are met: ... You, or a person affiliated with you, still owns, or has a right to buy,
    // the substituted property 30 calendar days after the sale

    // The Income Tax Act defines a superficial loss to be a loss from the sale of a particular property
    // where the same or identical property is acquired by the individual, or an affiliated person, during
    // the period beginning 30 calendar days before the sale and ending 30 calendar days after the sale.
    // At the end of that period, the individual or the affiliated person must continue to own the same property.
    // The amount of any capital loss that is deemed to be a superficial loss is added
    // to the adjusted cost base (ACB) of the substituted property.
    //
    //Here’s an example. Kyle bought 1000 XYZ mutual fund trust units with a NAV of $10.00 / unit on November 3, 2019.
    // On November 17, 2019, Kyle sold all 1000 trust units of XYZ mutual funds at $7.00 / unit.
    // On November 21, 2019, Kyle reacquired 1000 trust units of XYZ mutual funds at $6.00 / unit.
    // On December 17, 2019, Kyle still owned all 1000 units of XYZ mutual fund.
    //
    //Since Kyle acquired the identical property within 30 days of the sale of his 1000 units of XYZ mutual fund
    // and he still owned the investments 30 days after the sale,
    // Kyle has a superficial loss of $3,000 ($7,000 – $10,000).
    // The $3,000 capital loss is then added to the ACB of the newly acquired units.

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
                + "\nGains: " + DOLLAR_FORMAT.format(gains)
                + "\nTotalShares: " + newTotalShares
                + "\nACB: " + DOLLAR_FORMAT.format(getNewTotalACB()) + " CAD", date);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ticker", ticker);
        toJsonDate(json);
        json.put("isSell", isSell);
        json.put("value", value);
        json.put("isUSD", isUSD);
        json.put("fxRate", fxRate);
        json.put("shares", shares);
        json.put("commission", commission);

        return json;
    }

    private void toJsonDate(JSONObject json) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        json.put("year", year);
        json.put("month", month);
        json.put("day", day);
    }


}
