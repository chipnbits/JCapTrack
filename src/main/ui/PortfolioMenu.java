package ui;

import model.Portfolio;
import model.Security;
import model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import static ui.JCapTrack.DOLLAR_FORMAT;

// This is a system of menus for navigating through a portfolio
public class PortfolioMenu {
    private final Portfolio user;
    private Scanner input = new Scanner(System.in);

    public PortfolioMenu(Portfolio p) {
        user = p;
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Welcome to your portfolio " + p.getName());
        mainMenu();
    }

    // EFFECTS: Displays all of the valid options from the main menu
    private void printMainMenu() {
        System.out.println("Choose from the following menu options or press q to quit");
        System.out.println("1 - View holdings"
                + "\n2 - Add a security"
                + "\n3 - Remove a security"
                + "\n4 - Add a transaction"
                + "\n5 - Generate tax slips");
    }

    // EFFECTS: handles the selection of various options from main menu and selects the correct action
    private void mainMenu() {
        String cmd;
        boolean getInput = true;

        printMainMenu();

        while (getInput) {
            cmd = input.next();
            if (cmd.equals("q")) {
                System.out.println("Exiting portfolio");
                getInput = false;
            } else if (cmd.equals("1")) {
                displayHoldings();
            } else if (cmd.equals("2")) {
                addSecurityMenu();
            } else if (cmd.equals("3")) {
                removeSecurityMenu();
            } else if (cmd.equals("4")) {
                new TransactionEntryMenu(user);
            } else if (cmd.equals("5")) {
                taxGenerator();
            } else {
                System.out.println("Invalid option");
            }
        }
    }


    //EFFECTS: Displays the basic information on the securities held in the portfolio
    private void displayHoldings() {
        System.out.println("You are currently holding the following securities:");
        System.out.println("Name: || Shares: ||     ACB:    ||Transactions: ");
        for (Security s : user.getHoldings()) {
            System.out.format("%5s || %5d   || %10s  ||%2d\n", s.getTicker(), s.getShares(),
                    DOLLAR_FORMAT.format(s.getAcb()), s.getNumTransactions());
        }
    }

    // MODIFIES: this
    // EFFECTS: Add a new security to the portfolio
    private void addSecurityMenu() {
        ArrayList<String> names = new ArrayList<>();

        for (Security s : user.getHoldings()) {
            names.add(s.getTicker());
        }

        System.out.println("Enter the security name or enter q to exit:");
        String cmd = input.next();
        if (cmd.equals("q")) {
            System.out.println("exiting");
        } else {
            if (names.contains(cmd)) {
                System.out.println("That name is already taken");
                pressEnter();
            } else {
                System.out.println("Adding " + cmd + " to your holdings");
                user.addNewSecurity(new Security(cmd));
            }
        }
        printMainMenu();
    }


    //EFFECTS: Displays the securities to be removed or advises user there are none if needed
    private void removeSecurityMenu() {
        if (user.getHoldings().size() < 1) {
            System.out.println("You have no holdings!");
        } else {
            displayHoldings();
            System.out.println("Enter q to cancel or enter in a security name from the list");
            removeSecurityInput();
            System.out.println("Exiting menu");
            pressEnter();
        }
    }

    // MODIFIES: this
    // EFFECTS: Handles the user input for removing a security, will remove one from portfolio if required
    private void removeSecurityInput() {
        ArrayList<String> names = new ArrayList<>();
        boolean getInput = true;

        for (Security s : user.getHoldings()) {
            names.add(s.getTicker());
        }

        while (getInput) {
            String cmd = input.next();
            if (cmd.equals("q")) {
                getInput = false;
            } else if (names.contains(cmd)) {
                Security remove = user.getHoldings().get(names.indexOf(cmd));
                user.removeSecurity(remove);
                System.out.println("Removing the security:" + remove);
                getInput = false;
            } else {
                System.out.println("Not a valid option");
            }
        }
    }


    private void taxGenerator() {
    }

    private void pressEnter() {
        System.out.println("Press ENTER to continue...");
        input.nextLine();
    }
}
