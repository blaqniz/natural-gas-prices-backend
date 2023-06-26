package za.co.invoke.solutions.naturalgasprices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.invoke.solutions.naturalgasprices.entities.GasPrice;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GasPriceRepository extends JpaRepository<GasPrice, Long> {

    Optional<GasPrice> findGasPriceByDate(LocalDate date);
}
