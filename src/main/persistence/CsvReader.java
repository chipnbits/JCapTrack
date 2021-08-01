package persistence;


import exceptions.FileCorruptException;
import model.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Represents a reader that reads a portfolio from data stored in csv file from https://www.adjustedcostbase.ca/
// Adds the securities and transactions to an existing portfolio

/*
This implementation has  borrowed some code structure and concepts from JsonSerializationDemo UBC CPSC 210
In particular the readFile Method
 */

public class CsvReader {
    private static final String START_OF_NAMES_INDICATOR = "\"ACB/Share\"";
    private static final String START_OF_TRANSACTIONS_INDICATOR = "\"Security\",\"Date\"";
    private static final int YEAR_START_INDEX = 0;
    private static final int YEAR_END_INDEX = 4;
    private static final int MONTH_START_INDEX = 5;
    private static final int MONTH_END_INDEX = 8;
    private static final int DAY_START_INDEX = 9;
    private static final int DAY_END_INDEX = 11;

    private enum MonthTypeEnum {
        Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
    }

    private String source;

    // EFFECTS: constructs reader to read from source csv file
    public CsvReader(String source) {
        this.source = source;
    }

    // EFFECTS: creates a new scanner to read the source file and parse data from it
    private Scanner makeFileScanner(String source) throws FileNotFoundException {

        File importFile = new File(source);

        return new Scanner(importFile);
    }

    // EFFECTS: returns a package of data which is a list of security names and a list of transactions
    public ImportData parseData() throws FileNotFoundException, FileCorruptException {
        Scanner fileReader = makeFileScanner(source);
        List<String> tickers = parseSecurityNames(fileReader);
        List<Transaction> transactions = parseTransactions(fileReader);

        return new ImportData(tickers, transactions);
    }

    // MODIFIES: fileReader
    // EFFECTS: parses a complete list of security names from a csv file, consumes the securities portion of the
    //          data stream coming from file reader
    private List<String> parseSecurityNames(Scanner fileReader) throws FileCorruptException {
        List<String> tickers = new ArrayList<>();
        String line;
        try {
            while (fileReader.hasNextLine()) {
                // moves to the first instance of security listings
                if (fileReader.nextLine().contains(START_OF_NAMES_INDICATOR)) {
                    // Checks that a line begins with " indicating it is still in the securities section
                    while (fileReader.hasNextLine()) {
                        line = fileReader.nextLine();
                        if (line.length() > 0) {
                            // adds the substring between the two " marks to tickers
                            tickers.add(line.substring(1, line.indexOf('\"', 1)));
                        } else {
                            // When the empty line break is reached the while loop is exited
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new FileCorruptException("Unable to parse the securities properly");
        }
        return tickers;
    }

    // MODIFIES: fileReader
    // EFFECTS: parses a complete list of transactions from a csv file, consumes the transaction portion of the
    //          data stream coming from file reader
    private List<Transaction> parseTransactions(Scanner fileReader) throws FileCorruptException {
        List<Transaction> transactions = new ArrayList<>();
        String line;

        try {
            while (fileReader.hasNextLine()) {

                // moves to the first instance of transactions
                if (fileReader.nextLine().contains(START_OF_TRANSACTIONS_INDICATOR)) {
                    while (fileReader.hasNextLine()) {
                        line = fileReader.nextLine();
                        if (line.length() > 0) {
                            transactions.add(parseTransaction(line));
                        } else {
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new FileCorruptException("Unable to parse all transactions");
        }

        return transactions;
    }

    // EFFECTS: Parses the contained values from a csv transaction string and returns them as a string array.
    private Transaction parseTransaction(String line) throws FileCorruptException {
        List<String> values = new ArrayList<>();
        String value;
        // Make a regex pattern to find occurrences of values contained in between quotes
        Pattern quotes = Pattern.compile(("\"([^\"]*)\""));
        // Make a matcher for the string
        Matcher parser = quotes.matcher(line);

        // While there are still matches
        while (parser.find()) {
            // Save the matched value
            value = parser.group();
            // Remove the quotes
            value = value.substring(1, value.length() - 1);
            // Save value to array
            values.add(value);
        }
        return buildTransaction(values);
    }

    // Parses the primitive values from the strings and compiles them into a structured transaction
    private Transaction buildTransaction(List<String> values) throws FileCorruptException {
        String ticker = values.get(0);
        Calendar date = makeDate(values.get(1));
        boolean isSell = (values.get(2).equals("Sell"));
        double value = Double.parseDouble(values.get(3));
        boolean fx = (values.size() > 14);
        double fxRate = 0;
        if (fx) {
            fxRate = Double.parseDouble(values.get(14));
            value = Double.parseDouble(values.get(15));
        }

        int shares = Integer.parseInt(values.get(4));
        double commission;
        if (values.get(6).equals("")) {
            commission = 0;
        } else {
            commission = Double.parseDouble(values.get(6));
        }

        return new Transaction(ticker, date, isSell, value, fx, fxRate, shares, commission);
    }

    // EFFECTS: makes a new Calendar date from a parsed string
    private Calendar makeDate(String s) throws FileCorruptException {
        int year;
        int month;
        int day;
        Calendar date = Calendar.getInstance();

        try {

            year = Integer.parseInt(s.substring(YEAR_START_INDEX, YEAR_END_INDEX));
            MonthTypeEnum monthType = MonthTypeEnum.valueOf(s.substring(MONTH_START_INDEX, MONTH_END_INDEX));
            // Turn the parsed enumeration into an integer
            month = monthType.ordinal();
            day = Integer.parseInt(s.substring(DAY_START_INDEX, DAY_END_INDEX));
            date.set(year, month, day);

        } catch (Exception e) {
            throw new FileCorruptException("Unable to properly parse date");
        }

        return date;
    }


}
