package za.co.invoke.solutions.naturalgasprices.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GasPriceDto {

    private LocalDate date;
    private BigDecimal price;
}
