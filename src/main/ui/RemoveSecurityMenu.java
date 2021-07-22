package ui;

import model.Portfolio;

// This is the menu for removing securities from the portfolio
public class RemoveSecurityMenu extends MenuScreen {
    private final Portfolio user;

    // EFFECTS: initilize and open the main menu
    public RemoveSecurityMenu(Portfolio p) {
        menuName = "Remove Securities Menu";
        user = p;
        mainMenu();
    }

    @Override
    // EFFECTS: Displays all of the valid options from the main menu
    public void printMainMenu() {
        super.printMainMenu();
        PortfolioMenu.displayHoldings(user);
        System.out.println("Please enter a security to remove");
    }

    // MODIFIES: this
    // EFFECTS:  If a valid security is entered for removal it will take it out of the portfolio
    //          If there are no more securities then exit the menu.
    public boolean selectOption(String cmd) {
        boolean success = true;

        if (user.removeSecurity(cmd)) {
            System.out.println(cmd + " has been removed from your portfolio");
            pressEnter();
        } else {
            System.out.println("That security is not in your portfolio, invalid option");
            success = false;
        }
        return success;
    }

    // EFFECTS:  Checks to see if there are still any holdings to remove, if there aren't returns false
    @Override
    public boolean checkIfContinue() {
        return (user.getNumHoldings() > 0);
    }


}
