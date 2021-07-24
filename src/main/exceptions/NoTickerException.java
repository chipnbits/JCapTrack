package exceptions;

public class NoTickerException extends Exception {

    private final String badArgument;

    // EFFECTS: stores the argument that triggered the exception
    public NoTickerException(String badArgument) {
        this.badArgument = badArgument;
    }

    public String getBadArgument() {
        return badArgument;
    }




}
