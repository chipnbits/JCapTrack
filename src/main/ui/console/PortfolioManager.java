package ui.console;

import model.Portfolio;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// Assists JCapTrackMenu with managing opening, closing, saving, and loading of portfolios
public class PortfolioManager extends User {

    public static final String ACCOUNT_NAME_SAVE_LOCATION = "./data/accountIndex.json";
    public static final String PORTFOLIO_FILE_APPEND = "./data/portfolios/";

    private final JsonReader namesReader = new JsonReader(ACCOUNT_NAME_SAVE_LOCATION);
    private final JsonWriter namesWriter = new JsonWriter(ACCOUNT_NAME_SAVE_LOCATION);

    private List<String> names;  // Account names

    // EFFECTS: makes a new portfolio manager
    public PortfolioManager() {
        names = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: Adds a new portfolio to the program JCapTrackMenu
    protected void addNewPortfolio() {
        String cmd;
        boolean noMatch = true;

        while (noMatch) {
            System.out.println("Enter the name for the new portfolio");
            cmd = input.nextLine();

            if (names.contains(cmd)) {
                System.out.println("That name is already taken, choose a different one");
                // Alpha numeric account names
            } else if (cmd.matches("^[a-zA-Z0-9_]+$")) {
                System.out.println("Creating new portfolio");
                createNewPortfolio(cmd);

                noMatch = false;
            } else {
                System.out.println("That is not a valid name");
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: creates a new portfolio, adds it to profiles and account names list, and makes a new file location to
    // store data.
    protected void createNewPortfolio(String name) {

        Portfolio added = new Portfolio(name);
        writePortfolioSaveFile(added);
        addAccountName(added.getName());
    }

    // MODIFIES: this
    // EFFECTS: adds a new account name and saves it to the persistence file
    protected void addAccountName(String name) {
        if (!names.contains(name)) {
            names.add(name);
            saveAccountNames();
        }
    }


    // MODIFIES: this
    // EFFECTS: Opens the default save location for the stored account names, if unable to read the account names
    //          it creates a new empty save file
    protected void loadAccountNames() {

        try {
            names = namesReader.readList();
        } catch (IOException e) {
            System.out.println("There is no account names location, creating a new one");
            try {
                saveAccountNames();
                names = namesReader.readList();
            } catch (IOException ioException) {
                System.out.println("Unable to open account name file");
                ioException.printStackTrace();
            }
        }
    }

    // EFFECTS: Saves a portfolio to disk, notifies user if there is an error
    protected void savePortfolio(Portfolio p) {
        try {
            System.out.println("Writing your portfolio to disk...");
            writePortfolioSaveFile(p);
        } catch (Exception e) {
            System.out.println("Unable to save your data!");
            e.printStackTrace();
        }
    }

    // EFFECTS:  Saves all of the account names to a JSON file for retrieval, notifies user if there is an error
    protected void saveAccountNames() {

        try {
            namesWriter.open();
            namesWriter.write(this.names);
            namesWriter.close();
        } catch (IOException e) {
            System.out.println("Unable to save the list of portfolio names");
            e.printStackTrace();
        }
    }

    // REQUIRES: A portfolio name that is not already taken
    // MODIFIES: this
    // EFFECTS: makes a new specific file location to store data for the given portfolio,
    // notifies user if there is an error
    private void writePortfolioSaveFile(Portfolio p) {
        String location = getLocation(p.getName());
        try {
            JsonWriter writer = new JsonWriter(location);
            writer.open();
            writer.write(p);
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to create a new file location for that portfolio");
        }
    }

    // EFFECTS: takes a portfolio name and returns the correct file storage location
    private String getLocation(String name) {
        return PORTFOLIO_FILE_APPEND.concat(name).concat(".json");
    }

    // REQUIRES: A portfolio name from the portfolio list
    // MODIFIES: this
    // EFFECTS: Loads a portfolio from a saved file location
    protected Portfolio loadPortfolioSaveFile(String name) throws IOException {
        String fileLocation = getLocation(name);
        JsonReader reader = new JsonReader(fileLocation);
        return reader.readPortfolio();
    }

    protected List<String> getNames() {
        return names;
    }

}
