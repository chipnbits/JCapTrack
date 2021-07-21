package ui;

import model.Portfolio;

// Creates a menu for navigating adding a new security
public class AddSecurityMenu extends MenuScreen {
    private final Portfolio user;

    public AddSecurityMenu(Portfolio p) {
        menuName = "Portfolio Menu";
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
    public boolean selectOption(String cmd) {
        boolean success = true;

        if (user.addNewSecurity(cmd)) {
            System.out.println(cmd + " has been added to your portfolio");
            pressEnter();
        } else {
            System.out.println("That security is already in your portfolio.");
            System.out.println("Please enter a new security ticker symbol to add");
            success = false;
        }
        return success;
    }

}
