package persistence;

import model.Portfolio;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

/*
This JSON implementation has  borrowed some code structure and concepts from JsonSerializationDemo UBC CPSC 210
 */

// Represents a writer that writes JSON representation of workroom to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of portfolio to file
    public void write(Portfolio p) {
        JSONObject json = p.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of a list of account names
    public void write(List<String> list) {
        JSONObject json = listOfStringToJson(list);
        saveToFile(json.toString(TAB));
    }

    // EFFECTS: Coverts a list of account names into a single JSONObject and returns it
    private JSONObject listOfStringToJson(List<String> list) {
        JSONObject json = new JSONObject();

        //Make an array from the names list
        JSONArray accountNames = new JSONArray();
        for (String s : list) {
            accountNames.put(s);
        }
        json.put("names", accountNames);

        return json;
    }


    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
