package za.co.invoke.solutions.naturalgasprices.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionConstantsEnum {

    BAD_REQUEST("Bad token request.", HttpStatus.BAD_REQUEST, ErrorTypeEnum.FATAL),
    KEY_PAIR("No Such Algorithm Exception.", HttpStatus.NOT_FOUND, ErrorTypeEnum.FATAL),
    TOKEN_NEVER_ISSUED_FOR_ID("Token never issued or token does not belong to requesting id.", HttpStatus.NOT_FOUND, ErrorTypeEnum.FATAL),
    TOKEN_NEVER_ISSUED_FOR_KEY_ID("Token never issued or token does not belong to key id in the token.", HttpStatus.NOT_FOUND, ErrorTypeEnum.FATAL),
    TOKEN_DECODE_EXCEPTION("Could not decode the token as it is not in the expected format.", HttpStatus.NOT_FOUND, ErrorTypeEnum.FATAL),
    MISSING_API_KEY_EXCEPTION("Missing API key from the Headers.", HttpStatus.NOT_FOUND, ErrorTypeEnum.FATAL),
    TOKEN_EXPIRED("Token is expired.", HttpStatus.ACCEPTED, ErrorTypeEnum.FATAL),
    DB_ERROR_OCCURRED("Database Error Occurred.", HttpStatus.INTERNAL_SERVER_ERROR, ErrorTypeEnum.FATAL);

    private final String message;
    private final HttpStatus httpStatus;
    private final ErrorTypeEnum errorType;

    /**
     * Instantiates a new error
     *
     * @param message    the message
     * @param httpStatus the httpStatus
     * @param errorType  the errorType
     */
    ExceptionConstantsEnum(String message, HttpStatus httpStatus, ErrorTypeEnum errorType) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorTypeEnum getErrorType() {
        return errorType;
    }
}
