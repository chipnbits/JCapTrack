package ui.console.portfolio;

import exceptions.FileCorruptException;
import model.Portfolio;
import persistence.CsvReader;
import persistence.ImportData;
import ui.console.MenuScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportDataMenu extends MenuScreen {
    private static final String DATA_FILE_EXTENSION = ".csv";
    private static final String IMPORT_DIRECTORY = "./data/csv";
    private final List<String> names;

    public ImportDataMenu(Portfolio user) {
        super(user);
        menuName = "Import CSV Menu";
        names = getNames();
        if (names.isEmpty()) {
            System.out.println("There are no " + DATA_FILE_EXTENSION + " import files available in the "
                    + IMPORT_DIRECTORY + " directory");
        } else {
            mainMenu();
        }
    }

    @Override
    // EFFECTS: Displays current holdings and instructions
    public void printMainMenu() {
        super.printMainMenu();
        System.out.println("Make sure that the import file is located in the " + IMPORT_DIRECTORY + " directory");
        System.out.println("Select one of the following files to import:\n");
        printPathNames();
    }

    // EFFECTS: Prints out the csv file names, if there are none available, notifies the user.
    private void printPathNames() {
        for (String s : names) {
            System.out.println(s);
        }
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Adds csv import data to user portfolio from a file or else notifies the user that the file location is
    //          not available.
    //          Returns true if successful at importing the data, false otherwise
    protected boolean selectOption(String cmd) {
        if (names.contains(cmd)) {
            importUserSelection(cmd);
            pressEnter();
            return true;
        } else {
            System.out.println("That file does not exist. Invalid option.");
            return false;
        }
    }




    // EFFECTS: Gets a list of csv files from the data import directory
    private List<String> getNames() {

        List<String> names = new ArrayList<>();
        File dir = new File(IMPORT_DIRECTORY);

        if (dir.list() == null) {
            System.out.println("Error in the file directory path");
        } else {
            names = new ArrayList<>(Arrays.asList(dir.list()));
            // Select only csv files
            names.removeIf(s -> !s.contains(DATA_FILE_EXTENSION));
        }

        return names;


    }


    // MODIFIES: this
    // EFFECTS: Imports the data from the selected file in cmd to this portfolio
    private void importUserSelection(String cmd) {
        try {
            ImportData data = new CsvReader(IMPORT_DIRECTORY + "/" + cmd).parseData();
            data.addToPortfolio(portfolio);
            System.out.println("Import was successful with " + data.getSecurityNames().size()
                    + " securities added and " + data.getTransactions().size() + " transactions added.");
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found, sorry");
        } catch (FileCorruptException e) {
            System.out.println("That file has corrupted data or does not match the correct format");
        }

    }

    @Override
    // EFFECTS:  Checks if the menu should be closed after successfully completing a task
    // returns true if continue or false if the menu should close
    protected boolean checkIfContinue() {
        return false;
    }


}
