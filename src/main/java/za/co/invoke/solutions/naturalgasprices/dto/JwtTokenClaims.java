package za.co.invoke.solutions.naturalgasprices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtTokenClaims {

    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private String uniqueId;
    private List<String> roles;
}
