package ui.gui.securities;

import model.Security;
import model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionDataValidator {

    private List<String> foundErrors;

    private String ticker;        // Name of Security

    private Calendar date;        // Date of transaction
    private boolean isSell;       // true for Sell
    private double value;         // Trade value in CAD or USD

    private boolean isUSD;        // true for USD
    private double fxRate;        // The USD to CAD exchange rate on the date of transaction

    private int shares;           // The number of shares exchanged
    private double commission;    // The commission charged by brokerage

    public TransactionDataValidator() {
        foundErrors = new ArrayList<>();
    }

    // MODIFIES: this, security
    // EFFECTS: Validates all the user date fields and adds the transaction if valid
    //          Returns true if new transaction was made, otherwise false

    public boolean validateEntries(Security security, Calendar date, boolean isSell, boolean isUSD,
                                   String valDollar, String valCents, String sharesStr, String comDollar,
                                   String comCents, String fxEntry) {
        this.ticker = security.getTicker();
        this.isSell = isSell;
        this.isUSD = isUSD;

        boolean valid;
        valid = validateDate(date);
        valid = validateValue(valDollar, valCents) && valid;
        valid = validateShares(sharesStr) && valid;
        valid = validateCommission(comDollar, comCents) && valid;
        valid = validatefxRate(fxEntry) && valid;

        return valid;
    }

    protected void addTransaction(Security security) {
        Transaction transAdd = new Transaction(ticker, this.date, this.isSell, value,
                this.isUSD, fxRate, shares, commission);
        security.addTransaction(transAdd);
    }


    // MODIFIES: this
    // EFFECTS: Validates the date information given and adds it to date field.  returns false if null value given

    private boolean validateDate(Calendar date) {
        if (date == null) {
            foundErrors.add("Date selection is invalid");
            return false;
        } else {
            this.date = date;
            return true;
        }
    }
    // MODIFIES: this
    // EFFECTS: Validates the value information given and parses it if possible, returns false otherwise

    private boolean validateValue(String valDollar, String valCents) {
        try {
            value = parseDollars(valDollar, valCents);
            return true;
        } catch (NumberFormatException e) {
            foundErrors.add("Value does not contain a valid dollar and cents amount");
            return false;
        }
    }
    // MODIFIES: this
    // EFFECTS: Validates the shares information given and parses it if possible, returns false otherwise

    private boolean validateShares(String sharesStr) {
        if (sharesStr.equals("")) {
            foundErrors.add(("Shares was left blank"));
            return false;
        } else {
            try {
                shares = Integer.parseInt(sharesStr);
                return shares >= 0;
            } catch (NumberFormatException e) {
                foundErrors.add("Shares doesn't have integer value");
                return false;
            }
        }
    }
    // MODIFIES: this
    // EFFECTS: Validates the commission information given and parses it if possible, returns false otherwise

    private boolean validateCommission(String comDollar, String comCents) {
        try {
            commission = parseDollars(comDollar, comCents);
            return true;
        } catch (NumberFormatException e) {
            foundErrors.add("Commission does not contain a valid dollar and cents amount");
            return false;
        }
    }
    // MODIFIES: this
    // EFFECTS: Validates the fx information given and parses it if possible, returns false otherwise

    private boolean validatefxRate(String fxEntry) {
        if (!isUSD) {
            fxRate = 0;
            return true;
        } else {
            try {
                fxRate = Double.parseDouble(fxEntry);
                return true;
            } catch (NumberFormatException e) {
                foundErrors.add("Invalid Fx Rate");
                return false;
            }
        }
    }
    // EFFECTS: Validates and parses a dollar mount from two strings

    private double parseDollars(String dollar, String cent) {
        int d;
        if (dollar.equals("")) {
            d = 0;
        } else {
            d = Integer.parseInt(dollar);
        }

        int c;
        if (cent.equals("")) {
            c = 0;
        } else {
            c = Integer.parseInt(cent);
        }

        if (c < 0 || c > 99 || d < 0) {
            throw new NumberFormatException();
        }

        return d * 1.0 + c * 1.0 / 100;
    }

    public List<String> getFoundErrors() {
        return foundErrors;
    }
}
