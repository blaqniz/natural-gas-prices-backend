package za.co.invoke.solutions.naturalgasprices.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;
import za.co.invoke.solutions.naturalgasprices.dto.GenericResponse;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * This class handles all exceptions in the Application.
 */

@Slf4j
@ControllerAdvice
public class ExceptionResolver extends ResponseEntityExceptionHandler {

    /**
     * Provides a generic exception handling mechanism.
     *
     * @param ex      The target exception
     * @param request The current request
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleGeneralException(final Exception ex, final WebRequest request) {
        log.error(ErrorConstants.LOG_MESSAGE, ex.getMessage());
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final GenericResponse<Object> errorResponse = getGenericResponse(ex.getMessage());
        return handleInternalException(ex, errorResponse, new HttpHeaders(), httpStatus, request);
    }

    /**
     * Handles all @{@link InvalidLoginCredentials}
     *
     * @param ex      The target exception
     * @param request The current request
     */
    @ExceptionHandler({InvalidLoginCredentials.class})
    public ResponseEntity<Object> handleInvalidLoginException(final InvalidLoginCredentials ex, final WebRequest request) {
        log.error(ErrorConstants.LOG_MESSAGE, ex.getMessage());
        final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        final GenericResponse<Object> errorResponse = getGenericResponse(ex.getMessage());
        return handleInternalException(ex, errorResponse, new HttpHeaders(), httpStatus, request);
    }

    /**
     * Handles all @{@link JwtTokenException}
     *
     * @param ex      The target exception
     * @param request The current request
     */
    @ExceptionHandler({JwtTokenException.class})
    public ResponseEntity<Object> handleJwtTokenException(final JwtTokenException ex, final WebRequest request) {
        log.error(ErrorConstants.LOG_MESSAGE, ex.getMessage());
        final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        final GenericResponse<Object> errorResponse = getGenericResponse(ex.getMessage());
        return handleInternalException(ex, errorResponse, new HttpHeaders(), httpStatus, request);
    }

    /**
     * Handles all @{@link UserAlreadyExistsException}
     *
     * @param ex      The target exception
     * @param request The current request
     */
    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserExistsException(final UserAlreadyExistsException ex, final WebRequest request) {
        log.error(ErrorConstants.LOG_MESSAGE, ex.getMessage());
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final GenericResponse<Object> errorResponse = getGenericResponse(ex.getMessage());
        return handleInternalException(ex, errorResponse, new HttpHeaders(), httpStatus, request);
    }

    private ResponseEntity<Object> handleInternalException(final Exception ex, final GenericResponse<Object> errorResponse,
                                                           final HttpHeaders headers, final HttpStatus status,
                                                           final WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, SCOPE_REQUEST);
        }
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    private GenericResponse<Object> getGenericResponse(String message) {
        return new GenericResponse<>().withSuccessful(false).withMessage(message);
    }
}
