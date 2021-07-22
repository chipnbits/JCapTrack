package ui;

import java.util.Scanner;

// This is a general construct to present a menu of options to the user with the option to close it
// and return to the previous menu
public abstract class MenuScreen {
    protected Scanner input = new Scanner(System.in);
    protected String menuName;

    // EFFECTS: Prints a menu header
    protected void printMainMenu() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("      " + menuName);
        System.out.println("---------------------------------------------------------------------------");
    }

    // EFFECTS: Prints a menu of options and waits for a user input or q to quit
    public void mainMenu() {
        do {
            printMainMenu();
            System.out.println("\nEnter 'q' to quit");
        } while (getInput());
    }

    // EFFECTS: Gathers the user input from the keyboard until a valid input is entered.
    //          It returns true if the menu should stay open
    //          or returns false if it should close
    protected boolean getInput() {
        String cmd;
        boolean getInput = true;
        boolean keepGoing = true;

        while (getInput) {
            cmd = input.next();
            if (cmd.equals("q")) {
                System.out.println("Exiting " + menuName);
                keepGoing = false;
                getInput = false;
            } else {
                // If a valid command is selected, execute the command then check to see if the menu should
                // stay open with check if continue.
                if (selectOption(cmd)) {
                    keepGoing = checkIfContinue();
                    getInput = false;
                    // If no valid command is selected print the options again
                } else {
                    pressEnter();
                    printMainMenu();
                    System.out.println("\nEnter 'q' to quit");
                    keepGoing = true;
                }
            }
        }
        return keepGoing;
    }

    // EFFECTS:  Checks if the menu should be closed after successfully completing a task
    // returns true if continue or false if the menu should close
    protected boolean checkIfContinue() {
        return true;
    }


    // EFFECTS: returns true if a valid menu option is selected, false if not
    protected abstract boolean selectOption(String cmd);

    // EFFECTS: Prompts the user to press enter to continue
    protected void pressEnter() {
        System.out.println("Press ENTER to continue...");
        input.nextLine();
        input.nextLine();
    }


}
