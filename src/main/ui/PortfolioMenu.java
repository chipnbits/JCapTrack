package ui;

import model.Portfolio;

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
                + "\n5 - Search Transactions"

                //TODO add the mechanism to make end of year tax slips
                + "\n6 - Generate tax slips");
    }

    @Override
    // EFFECTS: Reads the keyboard input for a selected option and returns true
    //          If no valid option selected, returns false.
    protected boolean selectOption(String cmd) {
        boolean success = true;

        if (cmd.equals("1")) {
            displayHoldings(user);
            pressEnter();
        } else if (cmd.equals("2")) {
            new AddSecurityMenu(user);
        } else if (cmd.equals("3")) {
            checkRemoveSecurity();
        } else if (cmd.equals("4")) {
            new TransactionEntryMenu(user);
        } else if (cmd.equals("5")) {
            new TransactionSearchMenu(user);
        } else if (cmd.equals("6")) {
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

    //TODO make tax slips for all transactions from a year
    private void taxGenerator() {
    }


    //EFFECTS: Displays the basic information on the securities held in the portfolio
    protected static void displayHoldings(Portfolio user) {
        System.out.println("You are currently holding the following securities:");
        System.out.println("Name: || Shares: ||     ACB:    ||Transactions: ");
        for (String s : user.getSummary()) {
            System.out.format(s);
        }
    }


}



