package ui.console.portfolio;

import model.Portfolio;
import ui.console.MenuScreen;

// This is the menu for removing securities from the portfolio
public class RemoveSecurityMenu extends MenuScreen {

    // EFFECTS: initialize and open the main menu
    public RemoveSecurityMenu(Portfolio user) {
        super(user);
        menuName = "Remove Securities Menu";
        mainMenu();
    }

    @Override
    // EFFECTS: Displays all of the valid options from the main menu
    public void printMainMenu() {
        super.printMainMenu();
        PortfolioMenu.displayHoldings(portfolio);
        System.out.println("Please enter a security to remove");
    }

    // MODIFIES: this
    // EFFECTS:  If a valid security is entered for removal it will take it out of the portfolio
    //          If there are no more securities then exit the menu.
    //          returns true if a security was found and removed, false otherwise
    public boolean selectOption(String cmd) {
        if (portfolio.removeSecurity(cmd)) {
            System.out.println(cmd + " has been removed from your portfolio");
            pressEnter();
            return true;
        } else {
            System.out.println("That security is not in your portfolio, invalid option");
            return false;
        }
    }

    // EFFECTS:  Checks to see if there are still any holdings to remove, if there aren't returns false
    @Override
    public boolean checkIfContinue() {
        return (portfolio.getNumHoldings() > 0);
    }


}
