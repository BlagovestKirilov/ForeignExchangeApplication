package com.app.foreignexchangeapplication.repository;

import com.app.foreignexchangeapplication.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findFirstByCurrencyCode(String currencyCode);
}
