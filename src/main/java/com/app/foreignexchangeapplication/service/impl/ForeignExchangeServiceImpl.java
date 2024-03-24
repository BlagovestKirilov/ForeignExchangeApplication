package com.app.foreignexchangeapplication.service.impl;

import com.app.foreignexchangeapplication.entity.Currency;
import com.app.foreignexchangeapplication.entity.CurrencyConversion;
import com.app.foreignexchangeapplication.entity.dto.CurrencyConversionDto;
import com.app.foreignexchangeapplication.entity.dto.CurrencyDto;
import com.app.foreignexchangeapplication.repository.CurrencyConversionRepository;
import com.app.foreignexchangeapplication.repository.CurrencyRepository;
import com.app.foreignexchangeapplication.service.CurrencyLayerService;
import com.app.foreignexchangeapplication.service.ForeignExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ForeignExchangeServiceImpl implements ForeignExchangeService {
    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyLayerService currencyLayerService;
    private final Map<String, BigDecimal> exchangeRateCache = new ConcurrentHashMap<>();

    private final Map<String, String> currencyMap = new ConcurrentHashMap<>();

    @Override
    public CurrencyConversionDto exchange(CurrencyConversionDto currencyConversionDto) throws IOException {
        String currencyPair = currencyConversionDto.getSourceCurrencyCode() + "_" + currencyConversionDto.getTargetCurrencyCode();
        BigDecimal convertedAmount;
        if (exchangeRateCache.containsKey(currencyPair)) {
            convertedAmount = exchangeRateCache.get(currencyPair);
        } else {
            currencyConversionDto.setAmountSourceCurrency(BigDecimal.ONE);
            convertedAmount = BigDecimal.valueOf(currencyLayerService.makeConvertRequest(currencyConversionDto.getSourceCurrencyCode(), currencyConversionDto.getAmountSourceCurrency(), currencyConversionDto.getTargetCurrencyCode()));
            exchangeRateCache.put(currencyPair, convertedAmount);
        }
        currencyConversionDto.setAmountTargetCurrency(convertedAmount);
        return currencyConversionDto;
     }

    @Override
    public CurrencyConversionDto convert(CurrencyConversionDto currencyConversionDto) throws IOException {
        BigDecimal convertedAmount = BigDecimal.valueOf(currencyLayerService.makeConvertRequest(currencyConversionDto.getSourceCurrencyCode(), currencyConversionDto.getAmountSourceCurrency(),currencyConversionDto.getTargetCurrencyCode()));
        currencyConversionDto.setAmountTargetCurrency(convertedAmount);
        CurrencyConversion transaction = new CurrencyConversion(currencyRepository.findFirstByCurrencyCode(currencyConversionDto.getSourceCurrencyCode()),currencyConversionDto.getAmountSourceCurrency(),currencyRepository.findFirstByCurrencyCode(currencyConversionDto.getTargetCurrencyCode()),convertedAmount);
        currencyConversionRepository.save(transaction);
        return currencyConversionDto;
    }
    @Override
    public List<CurrencyConversionDto> findTransactions(String transactionIdentifier, Date transactionDate){
        List<CurrencyConversion> currencyConversions = new ArrayList<>();
        if(Objects.nonNull(transactionIdentifier) && !transactionIdentifier.isEmpty() && Objects.nonNull(transactionDate)){
            currencyConversions = currencyConversionRepository.findByCurrencyConversionIdAndCurrencyConversionDate(transactionIdentifier.trim(), transactionDate);
        } else if (Objects.nonNull(transactionIdentifier) &&  !transactionIdentifier.isEmpty() ) {
            currencyConversions = currencyConversionRepository.findByTransactionIdentifier(transactionIdentifier.trim());
        } else if (Objects.nonNull(transactionDate)) {
            currencyConversions = currencyConversionRepository.findByCurrencyConversionDate(transactionDate);
        }
        List<CurrencyConversionDto> currencyConversionDtos = new ArrayList<>();
        for(CurrencyConversion currencyConversion : currencyConversions){
            CurrencyConversionDto dto = new CurrencyConversionDto();
            dto.setTransactionIdentifier(currencyConversion.getTransactionIdentifier());
            dto.setSourceCurrencyCode(currencyConversion.getSourceCurrency().getCurrencyCode());
            dto.setTargetCurrencyCode(currencyConversion.getTargetCurrency().getCurrencyCode());
            dto.setAmountSourceCurrency(currencyConversion.getAmountSourceCurrency());
            dto.setAmountTargetCurrency(currencyConversion.getAmountTargetCurrency());
            currencyConversionDtos.add(dto);
        }
        return currencyConversionDtos;
    }
    @Override
    public String[] getAvailableCurrencies(){
        if(currencyMap.isEmpty()) {
            List<CurrencyDto> availableCurrenciesDtos = currencyLayerService.getAllAvailableCurrencies();
            for (CurrencyDto currency : availableCurrenciesDtos) {
                currencyMap.put(currency.getCurrencyCode(), currency.getCurrencyName());
                currencyRepository.save(new Currency(currency.getCurrencyCode(), currency.getCurrencyName()));
            }
            return availableCurrenciesDtos.stream()
                    .map(CurrencyDto::getCurrencyCode)
                    .sorted()
                    .toArray(String[]::new);
        }else {
            return currencyMap.keySet().stream()
                    .sorted()
                    .toArray(String[]::new);
        }
    }
}
