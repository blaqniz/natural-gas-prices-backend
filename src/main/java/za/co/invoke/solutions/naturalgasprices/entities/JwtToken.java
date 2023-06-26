package za.co.invoke.solutions.naturalgasprices.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "jwt_token")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jwt_token_id")
    private Long id;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;

    @Lob
    @Column(name = "token", length = 1000, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public JwtToken(LocalDateTime issuedAt, LocalDateTime expiresAt, String token, User user) {
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.token = token;
        this.user = user;
    }
}
