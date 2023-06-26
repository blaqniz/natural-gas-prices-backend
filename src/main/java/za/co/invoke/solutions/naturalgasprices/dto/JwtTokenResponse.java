package za.co.invoke.solutions.naturalgasprices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenResponse {

    @JsonProperty("token_data")
    private TokenData tokenData;
}
