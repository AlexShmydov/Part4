package usersExceptions;

public class BaseUsersException extends Exception {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ERROR_PREFIX = "\nERROR!\n";
    private static final String TEMPLATE = "%s%s%s%s";

    public BaseUsersException(String message) {
        super(String.format(TEMPLATE, ANSI_RED, ERROR_PREFIX, message, ANSI_RESET));
    }
}
