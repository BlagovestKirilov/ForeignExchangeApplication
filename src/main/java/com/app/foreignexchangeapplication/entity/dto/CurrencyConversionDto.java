package com.app.foreignexchangeapplication.entity.dto;

import java.math.BigDecimal;

public class CurrencyConversionDto {
    private String transactionIdentifier;
    private String sourceCurrencyCode;
    private BigDecimal amountSourceCurrency;
    private String targetCurrencyCode;
    private BigDecimal amountTargetCurrency;

    public String getTransactionIdentifier() {
        return transactionIdentifier;
    }

    public void setTransactionIdentifier(String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }

    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public void setSourceCurrencyCode(String sourceCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode;
    }

    public BigDecimal getAmountSourceCurrency() {
        return amountSourceCurrency;
    }

    public void setAmountSourceCurrency(BigDecimal amountSourceCurrency) {
        this.amountSourceCurrency = amountSourceCurrency;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public BigDecimal getAmountTargetCurrency() {
        return amountTargetCurrency;
    }

    public void setAmountTargetCurrency(BigDecimal amountTargetCurrency) {
        this.amountTargetCurrency = amountTargetCurrency;
    }
}
