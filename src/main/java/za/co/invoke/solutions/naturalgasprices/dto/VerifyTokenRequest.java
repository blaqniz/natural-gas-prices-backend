package za.co.invoke.solutions.naturalgasprices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VerifyTokenRequest {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("public_key")
    private String publicKey;

    @JsonProperty("token")
    private String token;

    @JsonProperty("roles")
    private List<String> roles;

    public VerifyTokenRequest withUniqueId(Long userId) {
        this.userId = userId;
        return this;
    }

    public VerifyTokenRequest withPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public VerifyTokenRequest withToken(String token) {
        this.token = token;
        return this;
    }

    public VerifyTokenRequest withRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
