package ui.portfolio;


import model.Portfolio;
import model.Transaction;
import ui.MenuScreen;

import java.util.Calendar;
import java.util.Scanner;

// A menu that handles the entry of various fields to create a new transaction
public class TransactionEntryMenu extends MenuScreen {
    private String ticker;
    private Calendar date;
    private boolean type;
    private double val;
    private boolean fx;
    private double rate;
    private int shares;
    private double commission;

    public TransactionEntryMenu(Portfolio user) {
        super(user);
        menuName = "Transaction Entry Menu";
        mainMenu();
    }

    @Override
    // EFFECTS: Displays all of the valid options from the main menu, then prompts user to enter in
    //          All of the details and packages them into a transaction that is added to the portfolio
    public void printMainMenu() {
        super.printMainMenu();
        PortfolioMenu.displayHoldings(user);
        System.out.println("\nEnter in a valid ticker for the transaction");
    }

    @Override
    // EFFECTS: returns true if a valid ticker for transaction is selected, if a valid ticker is entered,
    //          then a new transaction is generated and added from user input.
    public boolean selectOption(String cmd) {
        boolean success = true;

        if (userTickerMatch(cmd)) {
            generateTransaction();
        } else {
            System.out.println("Invalid ticker for transaction entry");
            success = false;
        }
        return success;
    }

    // MODIFIES: this
    // EFFECTS: Requests all the details for a new transaction and packages them to be added to the user portfolio
    private void generateTransaction() {
        Transaction transAdd;

        getUserDate();
        System.out.println("Is the transaction a SELL order?");
        type = yesOrNo();
        System.out.println("What is the trade dollar amount of the transaction?");
        this.val = getDollars();
        getFx();
        shares = getUserShares();
        System.out.println("How much was commission on the trade?");
        commission = getDollars();
        transAdd = new Transaction(ticker, date, type, val, fx, rate, shares, commission);
        user.addTransaction(transAdd);
        System.out.println("Transaction added!");
        System.out.println(transAdd);
        pressEnter();
    }


    @Override
    protected boolean checkIfContinue() {
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Checks for a valid ticker to add to the transaction menu (this),
    // return true if valid ticker matches input command
    private boolean userTickerMatch(String cmd) {

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
        int month = getMonth() - 1; //Adjust the user input to calendar index 0-11
        int day = getDay();
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        this.date = date;
        String readBack = String.format("Date set to %1$tY-%1$tB-%1$td", date);
        System.out.println(readBack);
    }


    // EFFECTS: collect a valid year from the user, don't stop until they get it
    //          returns a valid year
    protected static int getYear() {
        Scanner input = new Scanner(System.in);

        String year;

        System.out.println("Enter a four digit year");

        do {
            //input.nextLine();
            year = input.findInLine("19\\d{2}|20\\d{2}");
            if (year == null) {
                System.out.println("Invalid year, enter a year 1900-2099");
            }
        } while (year == null);

        return Integer.parseInt(year);
    }


    // EFFECTS: collect a valid month from the user, don't stop until they get it!
    //          returns a valid month 1-12
    private int getMonth() {
        String month;

        System.out.println("Enter a two digit month from 01-12");

        do {
            input.nextLine();
            month = input.findInLine("(0[1-9])|(1[0-2])");
            if (month == null) {
                System.out.println("Invalid month");
            }
        } while (month == null);

        //Adjust for 0 based index
        return Integer.parseInt(month) - 1;
    }

    // EFFECTS: collect a valid day from the user, don't stop until they get it!
    //          returns a valid day 1-31
    private int getDay() {
        String day;

        System.out.println("Enter a two digit day from 01 to 31");

        do {
            input.nextLine();
            day = input.findInLine("(0[1-9])|[1-3][0-9]|31");
            if (day == null) {
                System.out.println("Invalid day");
            }
        } while (day == null);

        return Integer.parseInt(day);
    }


    // Effects: Parse a dollar amount from user, return the value
    private double getDollars() {
        String dollars;

        System.out.println("Please enter a valid dollar amount:");

        do {
            input.nextLine();
            dollars = input.findInLine("\\d*.\\d{2}|0");
            if (dollars == null) {
                System.out.println("Invalid entry, use format 1.99 or 8983.00 for example");
            }
        } while (dollars == null);

        return Double.parseDouble(dollars);
    }

    // MODIFIES: this
    // EFFECTS:  Asks the user forex information and updates this to reflect it.  If it is a USD transaction,
    //           requests the exchange rate
    private void getFx() {

        System.out.println("Is the transaction in USD?");

        if (yesOrNo()) {
            fx = true;
            rate = getFxRate();
        } else {
            fx = false;
        }
    }

    // EFFECTS: Parse a foreign exchange rate
    private double getFxRate() {
        String fx;

        System.out.println("What is the exchange rate on the day, 1 USD = ? CAD");

        do {
            input.nextLine();
            fx = input.findInLine("\\d.\\d*");
            if (fx == null) {
                System.out.println("Invalid rate");
            }
        } while (fx == null);

        return Double.parseDouble(fx);
    }

    // EFFECTS: Parse an integer value from the user for shares
    private int getUserShares() {
        String shares;

        System.out.println("How many shares were involved in the transaction, enter a positive integer");

        do {
            input.nextLine();
            shares = input.findInLine("\\d*");
            if (shares == null) {
                System.out.println("Invalid entry, try 10 or 205 for example");
            }
        } while (shares == null);

        return Integer.parseInt(shares);
    }

    // EFFECTS: Parse a yes or no command from the user, return y for yes, 'n' for no, 'f' for failure to read
    private boolean yesOrNo() {
        String answer;

        System.out.println("Enter 'y' or 'yes' for yes and 'n' or 'no' for no");

        do {
            input.nextLine();
            answer = input.findInLine("[yn]");
            if (answer == null) {
                System.out.println("Invalid option");
            }
        } while (answer == null);

        return (answer.equals("y"));

    }


}
