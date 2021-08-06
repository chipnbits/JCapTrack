package persistence;


import exceptions.DirectoryNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Reads and parses names for a filetype from a directory
public class FileFinder {

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

}
