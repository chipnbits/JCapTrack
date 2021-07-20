package ui;

import model.Portfolio;
import model.Security;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

// A menu that handles the entry of various fields to create a new transaction
public class TransactionEntryMenu {
    private Portfolio user;
    Scanner input = new Scanner(System.in);

    public TransactionEntryMenu(Portfolio user) {
        this.user = user;
        mainMenu();
    }

    private void mainMenu() {
        Security add = selectSecurity();
        Calendar date = selectDate();
    }

    // MODIFIES: this
    // EFFECTS: Selects a security from the list of added securities.
    // Creates a new one and adds to portfolio if needed
    private Security selectSecurity() {
        Security add;
        ArrayList<String> names = new ArrayList<>();
        boolean getInput = true;

        for (Security s : user.getHoldings()) {
            names.add(s.getTicker());
        }

        String cmd = input.nextLine();
        if (names.contains(cmd)) {
            add = user.getHoldings().get(names.indexOf(cmd));
            System.out.println("You selected " + add.getTicker());
        } else {
            add = new Security(cmd);
            System.out.println("Adding new holding with that name");
            user.addNewSecurity(add);
        }
        return add;
    }

    //TODO figure out how to enter a date into the console
    private Calendar selectDate() {
        Calendar date = Calendar.getInstance();

        System.out.println("Enter the year");
        int year = input.nextInt();

        return date;
    }
}
