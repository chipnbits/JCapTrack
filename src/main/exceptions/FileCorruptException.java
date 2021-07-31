package exceptions;

public class FileCorruptException extends Exception {
    // EFFECTS: stores the argument that triggered the exception
    public FileCorruptException(String badArgument) {
        super(badArgument);
    }
}
