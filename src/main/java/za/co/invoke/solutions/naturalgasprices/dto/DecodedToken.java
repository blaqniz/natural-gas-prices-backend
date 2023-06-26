package za.co.invoke.solutions.naturalgasprices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DecodedToken {

    @JsonProperty
    private String uid;

    @JsonProperty
    private List<String> roles;
}
