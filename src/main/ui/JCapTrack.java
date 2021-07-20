package ui;

import model.Portfolio;
import model.Security;
import model.Transaction;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

// An application to track capital gains and acb values for stock trades
public class JCapTrack {
    // Number formatting for currency
    public static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.CANADA);
    private ArrayList<Portfolio> userProfiles = new ArrayList<>();
    private ArrayList<String> names;  // Account names
    private Scanner input = new Scanner(System.in);

    //From here I have borrowed some code from the TellerApp project for keyboard entry and console items

    // EFFECTS: runs JCapTrack
    public JCapTrack() {
        init();
        runJCapTrack();
    }

    // MODIFIES: this
    // EFFECTS: initializes a sample portfolio
    private void init() {
        Portfolio sample = new Portfolio("mm");
        Security bns = new Security("BNS");
        Security brk = new Security("BRK");
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        Calendar date3 = Calendar.getInstance();
        date1.set(2018, Calendar.NOVEMBER, 20);
        date2.set(2019, Calendar.JUNE, 5);
        date3.set(2021, Calendar.MARCH, 20);
        sample.addNewSecurity(bns);
        sample.addNewSecurity(brk);
        sample.addTransaction(new Transaction(bns, date1, false, 1089.18,
                false, 0, 10, 4.99));
        sample.addTransaction(new Transaction(bns, date2, true, 420.20,
                false, 0, 5, 4.99));
        sample.addTransaction(new Transaction(bns, date3, false, 1850.10,
                false, 0, 20, 4.99));
        userProfiles.add(sample);
        // Update the names list to start
        names = getAccountNames();
    }

    // MODIFIES: this
    // EFFECTS: process which portfolio to open
    private void runJCapTrack() {
        boolean getInput = true;
        String cmd = null;
        printWelcomeMenu();
        while (getInput) {
            cmd = input.nextLine();
            if (cmd.equals("quit")) {
                getInput = false;
            } else {
                selectPortfolio(cmd);
            }
        }
    }

    private ArrayList<String> getAccountNames() {
        names = new ArrayList<>();
        for (int i = 0; i < userProfiles.size(); i++) {
            names.add(userProfiles.get(i).getName());
        }
        return names;
    }

    private void printWelcomeMenu() {
        System.out.println("Hello and welcome!");
        System.out.println("please select 'new' for new portfolio or enter the name of an existing portfolio");
        System.out.println("You can select 'quit' to exit the program");
        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i));
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new portfolio or selects one as needed
    private void selectPortfolio(String cmd) {
        Portfolio open;
        if (cmd.equals("new")) {
            makeNewPortfolio();
            //Print updated menu
            printWelcomeMenu();
        } else if (names.contains(cmd)) {
            new PortfolioMenu(userProfiles.get(names.indexOf(cmd)));
        } else {
            System.out.println("Invalid selection");
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds a new portfolio to the program
    private void makeNewPortfolio() {
        String cmd;
        boolean noMatch = true;
        while (noMatch) {
            System.out.println("Enter the name for the new portfolio");
            cmd = input.nextLine();
            if (cmd.matches("^[a-zA-Z0-9_]+$")) {
                System.out.println("Creating new portfolio");
                userProfiles.add(new Portfolio(cmd));
                names.add(cmd);
                noMatch = false;
            } else if (names.contains(cmd)) {
                System.out.println("That name is already taken, choose a different one");
            } else {
                System.out.println("That is not a valid name");
            }
        }
    }




}






