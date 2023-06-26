package za.co.invoke.solutions.naturalgasprices.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import za.co.invoke.solutions.naturalgasprices.dto.GasPriceDto;
import za.co.invoke.solutions.naturalgasprices.dto.GasPricePageResponse;
import za.co.invoke.solutions.naturalgasprices.entities.GasPrice;
import za.co.invoke.solutions.naturalgasprices.exceptions.GasPriceDetailsNotFound;
import za.co.invoke.solutions.naturalgasprices.helper.ListToPageConverter;
import za.co.invoke.solutions.naturalgasprices.repositories.GasPriceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static za.co.invoke.solutions.naturalgasprices.constants.GasPriceConstants.GAS_PRICE_DETAILS_NOT_FOUND;

@Slf4j
@Service
public class GasPriceServiceImpl implements GasPriceService {

    private final GasPriceRepository gasPriceRepository;

    public GasPriceServiceImpl(GasPriceRepository gasPriceRepository) {
        this.gasPriceRepository = gasPriceRepository;
    }

    @Override
    public GasPricePageResponse getGasPrices(Pageable pageable) {

        try {
            Page<GasPrice> gasPrices = gasPriceRepository.findAll(pageable);

            List<GasPriceDto> gasPriceDtos = new ArrayList<>();

            gasPrices.forEach(gasPrice -> {
                GasPriceDto gasPriceDto = new GasPriceDto();
                BeanUtils.copyProperties(gasPrice, gasPriceDto);
                gasPriceDtos.add(gasPriceDto);
            });

            Page<GasPriceDto> priceDtoPage = ListToPageConverter.convert(gasPriceDtos, 0, pageable.getPageSize());

            return new GasPricePageResponse()
                    .withGasPriceObjects(priceDtoPage.getContent())
                    .withPageNumber(gasPrices.getNumber())
                    .withTotalPages(gasPrices.getTotalPages())
                    .withTotalItems((int) gasPrices.getTotalElements());
        } catch (Exception e) {
            log.error("An error has occurred {}", e);
            throw e;
        }
    }

    @Override
    public GasPriceDto getGasPriceByDate(LocalDate date) {
        try {
            final GasPrice gasPrice = gasPriceRepository.findGasPriceByDate(date)
                    .orElseThrow(() -> new GasPriceDetailsNotFound(GAS_PRICE_DETAILS_NOT_FOUND));

            final GasPriceDto gasPriceDto = new GasPriceDto();
            BeanUtils.copyProperties(gasPrice, gasPriceDto);
            return gasPriceDto;
        } catch (GasPriceDetailsNotFound e) {
            log.error(GAS_PRICE_DETAILS_NOT_FOUND + " {}", e);
            throw e;
        } catch (Exception e) {
            log.error("An error has occurred {}", e);
            throw e;
        }
    }
}
