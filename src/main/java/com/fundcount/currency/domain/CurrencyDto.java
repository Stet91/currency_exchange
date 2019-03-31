package com.fundcount.currency.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class CurrencyDto {

    Date date;
    Map<String, BigDecimal> rates;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
