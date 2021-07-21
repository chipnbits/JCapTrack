package ui;


import model.Portfolio;
import model.Transaction;

import java.util.Calendar;


// TODO Sort out the issues with scanner input
// A menu that handles the entry of various fields to create a new transaction
public class TransactionEntryMenu extends MenuScreen {
    private final Portfolio user;
    private String ticker;
    private Calendar date;
    private boolean type;
    private double val;
    private boolean fx;
    private double rate;
    private int shares;
    private double commission;

    public TransactionEntryMenu(Portfolio p) {
        menuName = "Transaction Entry Menu";
        user = p;
        mainMenu();
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Displays all of the valid options from the main menu, then prompts user to enter in
    //          All of the details and packages them into a transaction that is added to the portfolio
    public void printMainMenu() {
        PortfolioMenu.displayHoldings(user);
        super.printMainMenu();

        System.out.println("Enter in a valid ticker for the transaction");
        if (userTickerMatch()) {
            getUserDate();
            System.out.println("Is the transaction a SELL order?");
            type = yesOrNo();
            System.out.println("What is the trade dollar amount of the transaction?");
            this.val = getDollars();
            System.out.println("Is the transaction in USD?");
            this.fx = yesOrNo();
            if (fx) {
                rate = getFxRate();
            }
            shares = getUserShares();
            System.out.println("How much was commission on the trade?");
            commission = getDollars();
            user.addTransaction(new Transaction(ticker, date, type, val, fx, rate, shares, commission));
        } else {
            System.out.println("Invalid ticker for transaction entry");
        }
    }

    // MODIFIES: this
    // EFFECTS: Checks for a valid ticker to add to the transaction menu (this),
    // return true if valid ticker matches input command
    private boolean userTickerMatch() {
        String cmd = input.nextLine();
        if (!user.hasTicker(cmd)) {
            System.out.println("You do not have that security in your portfolio");
            return false;
        } else {
            ticker = cmd;
            return true;
        }
    }

    // MODIFIES: this
    // EFFECTS: collects a valid date from the user, the year, month, and day
    private void getUserDate() {
        int year = getYear();
        int month = getMonth();
        int day = getDay();
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        this.date = date;
        System.out.println(String.format("Date set to %1$tY-%1$tB-%1$td", date));
    }


    // EFFECTS: collect a valid year from the user, don't stop until they get it!
    private int getYear() {
        String cmd;
        System.out.println("Enter the year, e.g. 2008 : ");

        cmd = input.findInLine("19\\d{2}|20\\d{2}");
        if (cmd == null) {
            System.out.println("Invalid year, enter a year 1900-2099");
            return getYear();
        } else {
            return Integer.parseInt(cmd);
        }
    }

    // EFFECTS: collect a valid year from the user, don't stop until they get it!
    private int getMonth() {
        String cmd;
        //clear scanner
        input.nextLine();
        System.out.println("Enter a month from 1-12");

        cmd = input.findInLine("[1-9]|1[0-2]");
        if (cmd == null || Integer.parseInt(cmd) > 12) {
            System.out.println("Invalid month");
            return getMonth();
        } else {
            return Integer.parseInt(cmd);
        }
    }

    // EFFECTS: collect a valid day from the user, don't stop until they get it!
    private int getDay() {
        String cmd;

        // Clear scanner
        input.nextLine();
        System.out.println("Enter a day from 1 to 31");

        cmd = input.findInLine("[1-9]|[1-3][0-9]|31");
        if (cmd == null) {
            System.out.println("Invalid day");
            return getDay();
        } else {
            return Integer.parseInt(cmd);
        }
    }

    // EFFECTS: Parse a yes or no command from the user, return true for yes
    private boolean yesOrNo() {
        String cmd;

        System.out.println("Enter 'y' for yes and 'n' for no");
        cmd = input.nextLine();
        cmd = cmd.toLowerCase();
        if (cmd.equals("y")) {
            return true;
        } else if (cmd.equals("n")) {
            return false;
        } else {
            System.out.println("invalid command");
            return yesOrNo();
        }
    }

    // Effects: Parse a dollar amount from user, return the value
    private double getDollars() {
        String cmd;

        input.nextLine();
        System.out.println("Please enter a valid dollar amount:");
        cmd = input.findInLine("\\d*.\\d{2}|0");
        if (cmd == null) {
            System.out.println("Invalid entry, use format 1.99 or 8983.00 for example");
            return getDollars();
        } else {
            return Double.parseDouble(cmd);
        }
    }

    // Parse a foreign exchange rate
    private double getFxRate() {
        String cmd;
        System.out.println("What is the exchange rate on the day, 1 USD = ? CAD");
        cmd = input.findInLine("\\d.\\d*");
        if (cmd == null) {
            System.out.println("Invalid entry, use format 0.9834 or 1.357 for example");
            return getFxRate();
        } else {
            return Double.parseDouble(cmd);
        }
    }

    // EFFECTS: Parse an integer value for shares
    private int getUserShares() {
        String cmd;
        System.out.println("How many shares were involved in the transaction, enter a positive integer");
        cmd = input.findInLine("\\d*");
        if (cmd == null) {
            System.out.println("Invalid entry, try 10 or 205 for example");
            return getUserShares();
        } else {
            return Integer.parseInt(cmd);
        }
    }

    @Override
    protected boolean selectOption(String cmd) {
        return false;
    }
}
