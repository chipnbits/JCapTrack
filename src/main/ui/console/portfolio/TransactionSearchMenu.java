package ui.console.portfolio;

import exceptions.NoTickerException;
import model.Portfolio;
import ui.console.MenuScreen;

import java.util.List;

// Creates a menu for navigating adding a new security
public class TransactionSearchMenu extends MenuScreen {

    public TransactionSearchMenu(Portfolio user) {
        super(user);
        menuName = "Transaction Search Menu";
        mainMenu();
    }

    @Override
    // EFFECTS: Displays current holdings and instructions
    public void printMainMenu() {
        super.printMainMenu();
        System.out.println("You are currently holding the following securities");
        for (String str : portfolio.getTickers()) {
            System.out.println(str);
        }
        System.out.println("Please enter a security ticker to search for");
    }

    // MODIFIES: this
    // EFFECTS: Searches for and displays all the transactions for a security, or else notifies user nothing was found
    //          returns true if a security was found, false otherwise
    public boolean selectOption(String cmd) {

        if (portfolio.hasTicker(cmd)) {
            try {
                displayHistory(cmd);
            } catch (NoTickerException e) {
                System.out.println(e.getMessage());
            }
            pressEnter();
            return true;
        } else {
            System.out.println("That security is not in your portfolio. Invalid option");
            return false;
        }
    }

    // EFFECTS: Displays all of the transactions for a security in ordered by date
    private void displayHistory(String ticker) throws NoTickerException {
        List<String> record;

        if (!portfolio.hasTicker(ticker)) {
            throw new NoTickerException(ticker);
        } else {
            record = portfolio.searchTransactions(ticker);
            if (record.isEmpty()) {
                System.out.println("No transactions found for security " + ticker);
            } else {
                int index = 1;
                for (String str : record) {
                    System.out.println("TRANSACTION #" + index++);
                    System.out.println(str + "\n");
                }
            }
        }
    }

    // EFFECTS:  Checks if the menu should be closed after successfully completing a task
    // returns true if continue or false if the menu should close
    protected boolean checkIfContinue() {
        return false;
    }

}