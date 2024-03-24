package com.app.foreignexchangeapplication.service;

import com.app.foreignexchangeapplication.entity.dto.CurrencyDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface CurrencyLayerService {
    Double makeConvertRequest(String sourceCurrencyCode, BigDecimal amountSourceCurrencyCode, String targetCurrencyCode ) throws IOException;
    List<CurrencyDto> getAllAvailableCurrencies();
}
