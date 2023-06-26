package za.co.invoke.solutions.naturalgasprices.exceptions;

public class InvalidLoginCredentials extends RuntimeException {

    public InvalidLoginCredentials(String message) {
        super(message);
    }
}
