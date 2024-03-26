package com.app.foreignexchangeapplication.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
public class CurrencyConversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Currency sourceCurrency;

    @Column(nullable = false)
    private BigDecimal amountSourceCurrency;

    @ManyToOne
    private Currency targetCurrency;
    private BigDecimal amountTargetCurrency;

    @Column(nullable = false)
    private Date transactionDate;
    @Column(nullable = false)
    private String transactionIdentifier;

    public CurrencyConversion(Currency sourceCurrency, BigDecimal amountSourceCurrency, Currency targetCurrency, BigDecimal amountTargetCurrency) {
        this.sourceCurrency = sourceCurrency;
        this.amountSourceCurrency = amountSourceCurrency;
        this.targetCurrency = targetCurrency;
        this.amountTargetCurrency = amountTargetCurrency;
        this.transactionDate = new Date();
        this.transactionIdentifier = UUID.randomUUID().toString();
    }

    public CurrencyConversion() {

    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public BigDecimal getAmountSourceCurrency() {
        return amountSourceCurrency;
    }

    public void setAmountSourceCurrency(BigDecimal amountSourceCurrency) {
        this.amountSourceCurrency = amountSourceCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getAmountTargetCurrency() {
        return amountTargetCurrency;
    }

    public void setAmountTargetCurrency(BigDecimal amountTargetCurrency) {
        this.amountTargetCurrency = amountTargetCurrency;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionIdentifier() {
        return transactionIdentifier;
    }

    public void setTransactionIdentifier(String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }
}
