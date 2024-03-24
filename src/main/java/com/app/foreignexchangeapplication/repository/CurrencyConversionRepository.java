package com.app.foreignexchangeapplication.repository;

import com.app.foreignexchangeapplication.entity.CurrencyConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Long> {
    @Query("SELECT t FROM CurrencyConversion t WHERE YEAR(t.transactionDate) = YEAR(:transactionDate) AND MONTH(t.transactionDate) = MONTH(:transactionDate) AND DAY(t.transactionDate) = DAY(:transactionDate)")
    List<CurrencyConversion> findByCurrencyConversionDate(@Param("transactionDate") Date transactionDate);
    @Query("SELECT t FROM CurrencyConversion t WHERE t.transactionIdentifier = :transactionIdentifier")
    List<CurrencyConversion> findByTransactionIdentifier(@Param("transactionIdentifier") String transactionIdentifier);
    @Query("SELECT t FROM CurrencyConversion t WHERE t.transactionIdentifier = :transactionIdentifier AND YEAR(t.transactionDate) = YEAR(:transactionDate) AND MONTH(t.transactionDate) = MONTH(:transactionDate) AND DAY(t.transactionDate) = DAY(:transactionDate)")
    List<CurrencyConversion> findByCurrencyConversionIdAndCurrencyConversionDate(@Param("transactionIdentifier") String transactionIdentifier, @Param("transactionDate") Date transactionDate);
}
