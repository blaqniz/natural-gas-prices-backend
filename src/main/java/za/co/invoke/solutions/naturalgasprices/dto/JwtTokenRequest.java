package za.co.invoke.solutions.naturalgasprices.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtTokenRequest {

    private String username;
}
