package za.co.invoke.solutions.naturalgasprices.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.invoke.solutions.naturalgasprices.dto.GasPriceDto;
import za.co.invoke.solutions.naturalgasprices.dto.GasPricePageResponse;
import za.co.invoke.solutions.naturalgasprices.dto.GenericResponse;
import za.co.invoke.solutions.naturalgasprices.services.GasPriceService;

import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@Slf4j
@RestController
@RequestMapping("/api")
public class GasPriceController {

    private final GasPriceService gasPriceService;

    public GasPriceController(GasPriceService gasPriceService) {
        this.gasPriceService = gasPriceService;
    }

    @GetMapping("/gas-prices/{page}/{size}")
    public ResponseEntity<GenericResponse<GasPricePageResponse>> getGasPrices(@PathVariable("page") int page,
                                                                              @PathVariable("size") int size) {

        log.info("*** getGasPriceByDate START ***");

        Pageable pageable = PageRequest.of(page, size);

        GasPricePageResponse pricePageResponse = gasPriceService.getGasPrices(pageable);

        GenericResponse<GasPricePageResponse> response = new GenericResponse<>();
        response.setPayload(pricePageResponse);
        response.setSuccessful(true);

        log.info("*** getGasPriceByDate END *** ");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/gas-price")
    public ResponseEntity<GenericResponse<GasPriceDto>> getGasPriceByDate(@RequestParam("date")
                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {

        log.info("*** getGasPriceByDate START *** date {}", date);

        GenericResponse<GasPriceDto> response = new GenericResponse<>();
        response.setPayload(gasPriceService.getGasPriceByDate(date));
        response.setSuccessful(true);

        log.info("*** getGasPriceByDate END *** ");
        return ResponseEntity.ok(response);
    }
}
