package ui;

import model.Portfolio;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.Writable;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// An application to track capital gains and acb values for stock trades
public class JCapTrack extends MenuScreen implements Writable {
    // Number formatting for currency
    public static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.CANADA);
    private static final String ACCOUNT_NAME_SAVE_LOCATION = "./data/accountIndex.json";
    private static final String PORTFOLIO_FILE_APPEND = "./data/portfolios/";
    private List<String> names;  // Account names

    // EFFECTS: runs JCapTrack by retrieving any saved accounts and opening portfolio selection (main) menu
    public JCapTrack() {
        init();
        mainMenu();
    }

    // MODIFIES: this
    // EFFECTS: initializes a sample portfolio
    private void init() {
        menuName = "JCapTrack Portfolio Selection Screen";
        names = new ArrayList<>();
        loadAccountNames();
    }

    @Override
    // EFFECTS: Displays all of the valid options from the main menu
    public void printMainMenu() {
        super.printMainMenu();
        System.out.println("Hello and welcome!");
        System.out.println("please select 'new' for new portfolio or enter the name of an existing portfolio");
        for (String name : names) {
            System.out.println(name);
        }
    }

    // MODIFIES: this
    // EFFECTS: process which portfolio to open, or creates a new portfolio and adds it to this if needed
    //          returns true if a valid option was selected, false otherwise
    protected boolean selectOption(String cmd) {
        boolean success = true;

        if (cmd.equals("new")) {
            addNewPortfolio();
        } else if (names.contains(cmd)) {
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
            Portfolio openPortfolio = loadPortfolioSaveFile(cmd);
            // Open portfolio
            new PortfolioMenu(openPortfolio);
            // Save upon exiting
            try {
                System.out.println("Writing your portfolio to disk...");
                writePortfolioSaveFile(openPortfolio);
            } catch (Exception e) {
                System.out.println("Unable to save your data!");
                e.printStackTrace();
            }
        } catch (NoSuchFileException e) {
            System.out.println("Save file is missing for this portfolio");
            createNewPortfolio(cmd);
            pressEnter();
        } catch (IOException e) {
            System.out.println("Unable to load that portfolio");
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds a new portfolio to the program
    private void addNewPortfolio() {
        String cmd;
        boolean noMatch = true;

        // Scratch the previous input line
        input.nextLine();

        while (noMatch) {
            System.out.println("Enter the name for the new portfolio");
            cmd = input.nextLine();

            if (names.contains(cmd)) {
                System.out.println("That name is already taken, choose a different one");

                // Alpha numeric account names
            } else if (cmd.matches("^[a-zA-Z0-9_]+$")) {
                createNewPortfolio(cmd);
                names.add(cmd);
                saveAccountNames();

                noMatch = false;
            } else {
                System.out.println("That is not a valid name");
            }
        }
    }

    // REQUIRES: A portfolio name that is not already taken
    // MODIFIES: this
    // EFFECTS: creates a new portfolio, adds it to profiles, and makes a new file location to store data.
    private void createNewPortfolio(String name) {
        System.out.println("Creating new portfolio");
        Portfolio added = new Portfolio(name);
        writePortfolioSaveFile(added);
    }

    // EFFECTS:  Saves all of the account names to a JSON file for retrieval
    private void saveAccountNames() {
        JsonWriter writer = new JsonWriter(ACCOUNT_NAME_SAVE_LOCATION);
        try {
            writer.open();
            writer.write(this.names);
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to save the list of portfolio names");
            e.printStackTrace();
        }
    }


    // MODIFIES: this
    // EFFECTS: Opens the default save location for the stored account names, if unable to read the account names
    //          it creates a new empty save file
    private void loadAccountNames() {
        JsonReader reader = new JsonReader(ACCOUNT_NAME_SAVE_LOCATION);
        try {
            names = reader.readList();
        } catch (IOException e) {
            System.out.println("There is no account names location, creating a new one");
            try {
                saveAccountNames();
                names = reader.readList();
            } catch (IOException ioException) {
                System.out.println("Unable to open account name file");
                ioException.printStackTrace();
            }
        }
    }

    // REQUIRES: A portfolio name that is not already taken
    // MODIFIES: this
    // EFFECTS: makes a new specific file location to store data for the given portfolio
    private void writePortfolioSaveFile(Portfolio p) {
        String location = PORTFOLIO_FILE_APPEND.concat(p.getName()).concat(".json");
        try {
            JsonWriter writer = new JsonWriter(location);
            writer.open();
            writer.write(p);
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to create a new file location for that portfolio");
        }
    }

    // REQUIRES: A portfolio name from the portfolio list
    // MODIFIES: this
    // EFFECTS: Loads a portfolio from a saved file location
    private Portfolio loadPortfolioSaveFile(String name) throws IOException {
        String fileLocation = PORTFOLIO_FILE_APPEND.concat(name.concat(".json"));
        JsonReader reader = new JsonReader(fileLocation);

        return reader.readPortfolio();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        //Make an array from the names list
        JSONArray accountNames = new JSONArray();
        for (String s : names) {
            accountNames.put(s);
        }
        json.put("names", accountNames);

        return json;
    }
}












