package ui.console.portfolio;

import model.Portfolio;
import ui.console.MenuScreen;

import java.util.List;

public class PortfolioMenu extends MenuScreen {

    public PortfolioMenu(Portfolio user) {
        super(user);
        menuName = "Portfolio Menu";
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
                + "\n6 - Generate tax slips"
                + "\n7 - Import from CSV");
    }

    @Override
    // EFFECTS: Reads the keyboard input for a selected option and returns true
    //          If no valid option selected, returns false.
    protected boolean selectOption(String cmd) {
        boolean success = true;

        if (cmd.equals("1")) {
            displayHoldings(portfolio);
            pressEnter();
        } else if (cmd.equals("2")) {
            new AddSecurityMenu(portfolio);
        } else if (cmd.equals("3")) {
            checkRemoveSecurity();
        } else if (cmd.equals("4")) {
            new TransactionEntryMenu(portfolio);
        } else if (cmd.equals("5")) {
            new TransactionSearchMenu(portfolio);
        } else if (cmd.equals("6")) {
            taxGenerator(TransactionEntryMenu.getYear());
        } else if (cmd.equals("7")) {
            new ImportDataMenu(portfolio);
        } else {
            success = false;
        }

        return success;
    }

    // Checks if there is at least one security to remove, otherwise notifies user there aren't any left
    private void checkRemoveSecurity() {
        if (portfolio.getNumHoldings() > 0) {
            new RemoveSecurityMenu(portfolio);
        } else {
            System.out.println("Your portfolio is empty, there are no securities to remove");
            pressEnter();
        }
    }

    // Generates a list of all relevant tax slips for the year
    private void taxGenerator(int year) {
        List<String> transactions = portfolio.getTaxTransactions(year);

        System.out.println("There are " + transactions.size() + " tax slips in that year");
        if (transactions.size() > 0) {
            for (String s : portfolio.getTaxTransactions(year)) {
                System.out.println(s + "\n");
            }
        }
        pressEnter();

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



