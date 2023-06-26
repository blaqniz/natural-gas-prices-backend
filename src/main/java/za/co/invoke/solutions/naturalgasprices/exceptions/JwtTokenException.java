package za.co.invoke.solutions.naturalgasprices.exceptions;

import org.springframework.http.HttpStatus;
import za.co.invoke.solutions.naturalgasprices.enums.ExceptionConstantsEnum;

public class JwtTokenException extends RuntimeException {

    private static final long serialVersionUID = 8624344810222702368L;

    private final String errorDetails;
    private final ExceptionConstantsEnum exceptionConstantsEnum;

    public JwtTokenException(ExceptionConstantsEnum error, String message, String details) {
        super(message);
        exceptionConstantsEnum = error;
        errorDetails = details;
    }

    public JwtTokenException(ExceptionConstantsEnum error, String message, Throwable cause, String details) {
        super(message, cause);
        exceptionConstantsEnum = error;
        errorDetails = details;
    }

    public ExceptionConstantsEnum getExceptionConstantsEnum() {
        return exceptionConstantsEnum;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public HttpStatus getHttpStatus() {
        return exceptionConstantsEnum.getHttpStatus();
    }
}
