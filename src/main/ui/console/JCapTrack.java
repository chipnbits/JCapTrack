package ui.console;

import model.Portfolio;
import ui.console.portfolio.PortfolioMenu;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.NumberFormat;
import java.util.Locale;

// An application to track capital gains and acb values for stock trades
public class JCapTrack extends MenuScreen {
    // Number formatting for currency
    public static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.CANADA);
    private final PortfolioManager pm;

    // EFFECTS: runs JCapTrackMenu by retrieving any saved accounts and opening portfolio selection (main) menu
    public JCapTrack() {
        pm = new PortfolioManager(input);
        init();
        mainMenu();
    }

    // MODIFIES: this
    // EFFECTS: initializes a sample portfolio
    private void init() {
        menuName = "JCapTrackMenu Portfolio Selection Screen";
    }

    @Override
    // EFFECTS: Displays all of the valid options from the main menu
    public void printMainMenu() {
        super.printMainMenu();
        System.out.println("Hello and welcome!");
        System.out.println("please select 'new' for new portfolio or enter the name of an existing portfolio");
        for (String name : pm.getNames()) {
            System.out.println(name);
        }
    }

    // MODIFIES: this
    // EFFECTS: process which portfolio to open, or creates a new portfolio and adds it to this if needed
    //          returns true if a valid option was selected, false otherwise
    protected boolean selectOption(String cmd) {
        boolean success = true;

        if (cmd.equals("new")) {
            pm.addNewPortfolio();
        } else if (pm.getNames().contains(cmd)) {
            openPortfolio(cmd);
        } else {
            System.out.println("Invalid Option!");
            success = false;
        }
        return success;
    }

    // REQUIRES: A valid portfolio name and matching save file location
    // EFFECTS: Opens the main PortfolioMenu for the portfolio matching the given string, loads the data from file.
    //          When the user exits it automatically saves and rewrites the portfolio data
    private void openPortfolio(String cmd) {
        try {
            // Load the portfolio
            Portfolio openPortfolio = pm.loadPortfolioSaveFile(cmd);
            // Open portfolio
            new PortfolioMenu(openPortfolio);
            pm.savePortfolio(openPortfolio);

        } catch (NoSuchFileException e) {
            System.out.println("Save file is missing for this portfolio");
            pm.createNewPortfolio(cmd);
            pressEnter();
        } catch (IOException e) {
            System.out.println("Unable to load that portfolio");
            e.printStackTrace();
        }
    }

}












