package com.app.foreignexchangeapplication;

import com.app.foreignexchangeapplication.entity.dto.CurrencyConversionDto;
import com.app.foreignexchangeapplication.entity.dto.CurrencyDto;
import com.app.foreignexchangeapplication.repository.CurrencyConversionRepository;
import com.app.foreignexchangeapplication.service.impl.ForeignExchangeServiceImpl;
import com.app.foreignexchangeapplication.service.impl.CurrencyLayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ForeignExchangeApplicationTests {

    @Autowired
    private CurrencyLayerServiceImpl currencyLayerService;

    @Autowired
    private ForeignExchangeServiceImpl foreignExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExchange() throws IOException {
        CurrencyConversionDto currencyConversionDto = new CurrencyConversionDto();
        currencyConversionDto.setAmountSourceCurrency(BigDecimal.TEN);
        currencyConversionDto.setSourceCurrencyCode("USD");
        currencyConversionDto.setTargetCurrencyCode("EUR");

        CurrencyConversionDto resultCurrencyConversionDto = foreignExchangeService.exchange(currencyConversionDto);

        assertEquals(currencyConversionDto.getAmountSourceCurrency(),resultCurrencyConversionDto.getAmountSourceCurrency());
        assertEquals(currencyConversionDto.getSourceCurrencyCode(),resultCurrencyConversionDto.getSourceCurrencyCode());
        assertEquals(currencyConversionDto.getTargetCurrencyCode(),resultCurrencyConversionDto.getTargetCurrencyCode());
        assertTrue(Objects.nonNull(currencyConversionDto.getAmountTargetCurrency()));
    }

    @Test
    public void testConvert() throws IOException {
        BigDecimal amountSourceCurrency = BigDecimal.TEN;
        String sourceCurrencyCode = "USD";
        String targetCurrencyCode = "EUR";

        CurrencyConversionDto currencyConversionDto = new CurrencyConversionDto();
        currencyConversionDto.setAmountSourceCurrency(amountSourceCurrency);
        currencyConversionDto.setSourceCurrencyCode(sourceCurrencyCode);
        currencyConversionDto.setTargetCurrencyCode(targetCurrencyCode);

        CurrencyConversionDto resultTransactionDto = foreignExchangeService.convert(currencyConversionDto);

        assertEquals(currencyConversionDto.getAmountSourceCurrency(),resultTransactionDto.getAmountSourceCurrency());
        assertEquals(currencyConversionDto.getSourceCurrencyCode(),resultTransactionDto.getSourceCurrencyCode());
        assertEquals(currencyConversionDto.getTargetCurrencyCode(),resultTransactionDto.getTargetCurrencyCode());
        assertTrue(Objects.nonNull(currencyConversionDto.getAmountTargetCurrency()));
    }

    @Test
    public void testMakeConvertRequest() throws IOException {
        Double result = currencyLayerService.makeConvertRequest("USD",BigDecimal.TEN,"BGN");
        assertTrue(Objects.nonNull(result));
        assertTrue(result > 1.0);
    }
}
