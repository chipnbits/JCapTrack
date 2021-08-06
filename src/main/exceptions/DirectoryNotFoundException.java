package exceptions;

public class DirectoryNotFoundException extends Exception {

    // EFFECTS: stores the argument that triggered the exception
    public DirectoryNotFoundException(String badArgument) {
        super(badArgument);
    }

}
