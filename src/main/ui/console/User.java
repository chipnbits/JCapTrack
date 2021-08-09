package ui.console;

import model.Portfolio;

import java.util.Scanner;

// Represents the user input coming from Scanner
public abstract class User {
    protected Scanner input = new Scanner(System.in);
    protected Portfolio user;

    protected void setUser(Portfolio p) {
        user = p;
    }

    // EFFECTS: Prompts the user to press enter to continue
    protected void pressEnter() {
        input.nextLine();
        System.out.println("Press ENTER to continue...");
        input.nextLine();
    }


}
