package ui;

import model.Portfolio;

// Creates a menu for navigating adding a new security
public class AddSecurityMenu extends MenuScreen {
    private final Portfolio user;

    public AddSecurityMenu(Portfolio p) {
        menuName = "Add Security Menu";
        user = p;
        mainMenu();
    }

    @Override
    // EFFECTS: Displays current holdings and instructions
    public void printMainMenu() {
        super.printMainMenu();
        PortfolioMenu.displayHoldings(user);
        System.out.println("Please enter a new security ticker symbol to add");
    }

    // MODIFIES: this
    // EFFECTS: Adds a new security to user portfolio if entered or else notifies the user that it is a duplicate
    //          Returns true if successful at adding a security, false otherwise
    public boolean selectOption(String cmd) {

        if (user.addNewSecurity(cmd)) {
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
