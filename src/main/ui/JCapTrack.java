package ui;

import model.Portfolio;
import model.Security;
import model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

// An application to track capital gains and acb values for stock trades
public class JCapTrack {
    // Number formatting for currency
    public static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.CANADA);
    private ArrayList<Portfolio> userProfiles;
    private Scanner input = new Scanner(System.in);

    //From here I have borrowed some code from the TellerApp project for keyboard entry and console items
    // EFFECTS: runs JCapTrack
    public JCapTrack() {
        init();
        runJCapTrack();
    }

    // MODIFIES: this
    // EFFECTS: initializes a sample portfolio
    private void init() {
        Portfolio sample = new Portfolio("Mr. Sample");
        Security bns = new Security("BNS");
        Security brk = new Security("BRK");
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        Calendar date3 = Calendar.getInstance();
        date1.set(2018, Calendar.NOVEMBER, 20);
        date2.set(2019, Calendar.JUNE, 5);
        date3.set(2021, Calendar.MARCH, 20);
        sample.addNewSecurity(bns);
        sample.addNewSecurity(brk);
        sample.addTransaction(new Transaction(bns, date1, false, 1089.18,
                false, 0, 10, 4.99));
        sample.addTransaction(new Transaction(bns, date2, true, 420.20,
                false, 0, 5, 4.99));
        sample.addTransaction(new Transaction(bns, date3, false, 1850.10,
                false, 0, 20, 4.99));
        userProfiles.add(sample);
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runJCapTrack() {
        portfolioSelectMenu();


    }

    private void portfolioSelectMenu() {
        Portfolio select;
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < userProfiles.size(); i++) {
            names.add(userProfiles.get(i).getName());
        }
        System.out.println("\nHello and welcome"
                + "\nplease select 1 for new portfolio or enter the name of an existing portfolio");
        System.out.println("Valid account names:");
        for (String n : names) {
            System.out.println(n);
        }
        boolean validOption = false;
        do {
            String sel = input.nextLine();
            if (sel == "1") {
                select = makeNewPortfolio();
                validOption = true;
            } else if (names.contains(sel)) {
                select = userProfiles.get(names.indexOf(sel));
                validOption = true;
            } else {
                System.out.println("Invalid selection");
            }
        } while (!validOption);
    }

    private Portfolio makeNewPortfolio() {
    }


}






