package za.co.invoke.solutions.naturalgasprices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.invoke.solutions.naturalgasprices.entities.JwtToken;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String jwtToken);
}
