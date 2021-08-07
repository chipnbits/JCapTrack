package persistence;


import exceptions.DirectoryNotFoundException;
import model.Portfolio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Reads and parses names for a filetype from a directory
public class FileFinder {

    public static final String PORTFOLIO_DIRECTORY = "./data/portfolios";
    public static final String JSON_FILE_EXTENSION = ".json";
    public static final String CSV_FILE_EXTENSION = ".csv";

    // MODIFIES: this
    // EFFECTS: Gets a list of .json files that have been saved into portfolios data folder
    //          Makes an error message if there is an issue with the file path
    public static List<String> getNamesFromSystem(String directory, String extension)
            throws DirectoryNotFoundException {
        List<String> names;

        File dir = new File(directory);

        if (dir.list() == null) {
            throw new DirectoryNotFoundException(directory);
        } else {
            names = new ArrayList<>(Arrays.asList(dir.list()));
            // Select only json files
            names.removeIf(s -> !s.contains(extension));
        }

        names = removeFileExtension(names);

        return names;
    }

    private static List<String> removeFileExtension(List<String> names) {
        List<String> removedExtension = new ArrayList<>();

        for (String s : names) {
            removedExtension.add(s.substring(0, s.lastIndexOf('.')));
        }

        return removedExtension;
    }

    // MODIFIES: data
    // EFFECTS: trys to delete a given file, returns true if successful
    public static boolean deleteFile(String name) {
        File toDelete = new File(name);

        return toDelete.delete();
    }

    // MODIFIES: data
    // EFFECTS: allocates a new portfolio save location based on the portfolio name
    public static void writePortfolioSaveFile(Portfolio p, String filepath) {

        try {
            JsonWriter writer = new JsonWriter(filepath);
            writer.open();
            writer.write(p);
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to create a new file location for that portfolio");
        }
    }


}
