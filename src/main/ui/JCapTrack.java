package ui;

import model.Portfolio;
import model.Transaction;

import java.text.NumberFormat;
import java.util.*;

// An application to track capital gains and acb values for stock trades
public class JCapTrack extends MenuScreen {
    // Number formatting for currency
    public static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.CANADA);
    private List<Portfolio> userProfiles = new ArrayList<>();
    private List<String> names;  // Account names

    //From here I have borrowed some code from the TellerApp project for keyboard entry and console items

    // EFFECTS: runs JCapTrack by initializing a dummy portfolio and starting the Portfolio selection menu
    public JCapTrack() {
        menuName = "JCapTrack Portfolio Selection Screen";
        init();
        mainMenu();
    }

    // MODIFIES: this
    // EFFECTS: initializes a sample portfolio
    private void init() {
        Portfolio sample = new Portfolio("mm");
        String bns = "BNS";
        String brk = "BRK";
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

    @Override
    // EFFECTS: Displays all of the valid options from the main menu
    public void printMainMenu() {
        super.printMainMenu();
        System.out.println("Hello and welcome!");
        System.out.println("please select 'new' for new portfolio or enter the name of an existing portfolio");
        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i));
        }
    }

    // MODIFIES: this
    // EFFECTS: process which portfolio to open, or creates a new portfolio and adds it to this if needed
    //          returns true if a valid option was selected, false otherwise
    protected boolean selectOption(String cmd) {
        boolean success = true;

        if (cmd.equals("new")) {
            makeNewPortfolio();
        } else if (names.contains(cmd)) {
            new PortfolioMenu(userProfiles.get(names.indexOf(cmd)));
        } else {
            success = false;
        }
        return success;
    }

    // EFFECTS: returns a list of all of the portfolio names stored in JCapTrack
    private List<String> getAccountNames() {
        names = new ArrayList<>();
        for (int i = 0; i < userProfiles.size(); i++) {
            names.add(userProfiles.get(i).getName());
        }
        return names;
    }

    // MODIFIES: this
    // EFFECTS: Adds a new portfolio to the program
    private void makeNewPortfolio() {
        String cmd;
        boolean noMatch = true;

        // Scratch the previous input line
        input.nextLine();

        while (noMatch) {
            System.out.println("Enter the name for the new portfolio");
            cmd = input.nextLine();
            // Alpha numeric account names
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






