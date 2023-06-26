package za.co.invoke.solutions.naturalgasprices.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.co.invoke.solutions.naturalgasprices.dto.GasPriceDto;
import za.co.invoke.solutions.naturalgasprices.dto.GasPricePageResponse;

import java.time.LocalDate;

public interface GasPriceService {

    GasPricePageResponse getGasPrices(Pageable pageable);
    GasPriceDto getGasPriceByDate(LocalDate date);
}
