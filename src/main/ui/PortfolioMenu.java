package ui;

import model.Portfolio;
import model.Security;

import static ui.JCapTrack.DOLLAR_FORMAT;

public class PortfolioMenu extends MenuScreen {
    private final Portfolio user;

    public PortfolioMenu(Portfolio p) {
        menuName = "Portfolio Menu";
        user = p;
        mainMenu();
    }

    @Override
    // EFFECTS: Displays all of the valid options from the main menu
    public void printMainMenu() {
        super.printMainMenu();
        System.out.println("Choose from the following menu options or press q to quit");
        System.out.println("1 - View holdings"
                + "\n2 - Add a security"
                + "\n3 - Remove a security"
                + "\n4 - Add a transaction"
                + "\n5 - Generate tax slips");
    }

    @Override
    // EFFECTS: Reads the keyboard input for a selected option and returns true
    //          If no valid option selected, returns false.
    protected boolean selectOption(String cmd) {
        boolean success = true;

        if (cmd.equals("1")) {
            displayHoldings(user);
        } else if (cmd.equals("2")) {
            new AddSecurityMenu(user);
        } else if (cmd.equals("3")) {
            checkRemoveSecurity();
        } else if (cmd.equals("4")) {
            new TransactionEntryMenu(user);
        } else if (cmd.equals("5")) {
            taxGenerator();
        } else {
            success = false;
        }

        return success;
    }

    // Checks if there is at least one security to remove, otherwise notifies user there aren't any left
    private void checkRemoveSecurity() {
        if (user.getNumHoldings() > 0) {
            new RemoveSecurityMenu(user);
        } else {
            System.out.println("Your portfolio is empty, there are no securities to remove");
            pressEnter();
        }
    }

    private void taxGenerator() {
    }


    //EFFECTS: Displays the basic information on the securities held in the portfolio
    protected static void displayHoldings(Portfolio user) {
        System.out.println("You are currently holding the following securities:");
        System.out.println("Name: || Shares: ||     ACB:    ||Transactions: ");
        for (Security s : user.getHoldings()) {
            System.out.format("%5s || %5d   || %10s  ||%2d\n", s.getTicker(), s.getShares(),
                    DOLLAR_FORMAT.format(s.getAcb()), s.getNumTransactions());
        }
    }


}



