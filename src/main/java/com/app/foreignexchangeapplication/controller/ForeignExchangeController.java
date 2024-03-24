package com.app.foreignexchangeapplication.controller;

import com.app.foreignexchangeapplication.entity.dto.CurrencyConversionDto;
import com.app.foreignexchangeapplication.exception.ExternalServiceException;
import com.app.foreignexchangeapplication.service.impl.ForeignExchangeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class ForeignExchangeController {
    @Autowired
    private ForeignExchangeServiceImpl foreignExchangeService;

    private final String RETURN_TEMPLATE = "%s %s is %s %s.";

    @GetMapping("/")
    public String show() {
        return "redirect:/exchange";
    }
    @GetMapping("/exchange")
    public String exchange(Model model) {
        model.addAttribute("currencies", foreignExchangeService.getAvailableCurrencies());
        return "exchange_rates";
    }

    @PostMapping("/exchange")
    @ResponseBody
    public ResponseEntity<String> exchangeCurrency(@RequestBody CurrencyConversionDto transactionDto) {
        try {
            CurrencyConversionDto currencyConversionDto = foreignExchangeService.exchange(transactionDto);
            String result = String.format(RETURN_TEMPLATE,currencyConversionDto.getAmountSourceCurrency(),currencyConversionDto.getSourceCurrencyCode(),currencyConversionDto.getAmountTargetCurrency(),currencyConversionDto.getTargetCurrencyCode());
            return ResponseEntity.ok(result);
        } catch (ExternalServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred during currency exchange!\nCode: " +e.getCode() +"!\nMessage: "+ e.getMessage()+"!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred during currency exchange!\nMessage: "+ e.getMessage()+"!");
        }
    }

    @PostMapping("/convert")
    @ResponseBody
    public ResponseEntity<String> convertCurrency(@RequestBody CurrencyConversionDto transactionDto) {
        try {
            CurrencyConversionDto currencyConversionDto = foreignExchangeService.convert(transactionDto);
            String result = String.format(RETURN_TEMPLATE,currencyConversionDto.getAmountSourceCurrency(),currencyConversionDto.getSourceCurrencyCode(),currencyConversionDto.getAmountTargetCurrency(),currencyConversionDto.getTargetCurrencyCode());
            return ResponseEntity.ok(result);
        } catch (ExternalServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred during currency exchange!\nCode: " +e.getCode() +"!\nMessage: "+ e.getMessage()+"!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred during currency exchange!\nMessage: "+ e.getMessage()+"!");
        }
    }
    @GetMapping("/convert")
    public String showExchangeRates(Model model) {
        model.addAttribute("currencies", foreignExchangeService.getAvailableCurrencies());
        return "converter";
    }

    @GetMapping("/conversions")
    public String showTransactions() {
        return "conversions_history";
    }

    @PostMapping("/conversions")
    public String findTransactions(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                                   @RequestParam(required = false) String transactionIdentifier,
                                   Model model) {
        List<CurrencyConversionDto> currencyConversionDtos = foreignExchangeService.findTransactions(transactionIdentifier, date);

        model.addAttribute("currencyConversions", currencyConversionDtos);
        return "conversions_history";
    }
}
