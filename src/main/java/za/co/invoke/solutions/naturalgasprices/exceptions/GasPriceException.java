package za.co.invoke.solutions.naturalgasprices.exceptions;

public class GasPriceException extends Exception {

    public GasPriceException(String message, Throwable cause) {
        super(message, cause);
    }

    public GasPriceException(String message) {
        super(message);
    }
}
