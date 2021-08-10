package ui.console.portfolio;

import model.Portfolio;
import ui.console.MenuScreen;

// Creates a menu for navigating adding a new security
public class AddSecurityMenu extends MenuScreen {

    public AddSecurityMenu(Portfolio user) {
        super(user);
        menuName = "Add Security Menu";
        mainMenu();
    }

    @Override
    // EFFECTS: Displays current holdings and instructions
    public void printMainMenu() {
        super.printMainMenu();
        PortfolioMenu.displayHoldings(portfolio);
        System.out.println("Please enter a new security ticker symbol to add");
    }

    // MODIFIES: this
    // EFFECTS: Adds a new security to user portfolio if entered or else notifies the user that it is a duplicate
    //          Returns true if successful at adding a security, false otherwise
    public boolean selectOption(String cmd) {

        if (portfolio.addNewSecurity(cmd)) {
            System.out.println(cmd + " has been added to your portfolio");
            pressEnter();
            return true;
        } else {
            System.out.println("That security is already in your portfolio. Invalid option.");
            return false;
        }
    }

    // EFFECTS:  Checks if the menu should be closed after successfully completing a task
    // returns true if continue or false if the menu should close
    protected boolean checkIfContinue() {
        return false;
    }

}
