package com.app.foreignexchangeapplication.service;

import com.app.foreignexchangeapplication.entity.dto.CurrencyConversionDto;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ForeignExchangeService {
    CurrencyConversionDto exchange(CurrencyConversionDto currencyConversionDto) throws IOException;

    CurrencyConversionDto convert(CurrencyConversionDto currencyConversionDto) throws IOException;

    List<CurrencyConversionDto> findTransactions(String transactionIdentifier, Date transactionDate);

    String[] getAvailableCurrencies();
}
