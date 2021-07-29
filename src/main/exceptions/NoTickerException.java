package exceptions;

public class NoTickerException extends RuntimeException {

    // EFFECTS: stores the argument that triggered the exception
    public NoTickerException(String badArgument) {
        super(badArgument);
    }

}
