package za.co.invoke.solutions.naturalgasprices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericResponse<T> implements Serializable {

    private static final long serialVersionUID = -2009657402718117058L;
    private boolean successful;
    private String message;
    private T payload;

    public GenericResponse withSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public GenericResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public GenericResponse withPayload(T payload) {
        this.payload = payload;
        return this;
    }
}
