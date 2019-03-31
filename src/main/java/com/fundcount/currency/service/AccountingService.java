package com.fundcount.currency.service;

import com.fundcount.currency.domain.Currency;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AccountingService {

    private static BigDecimal SPREAD = new BigDecimal("0.64");

    private FixerService fixerService;

    @Autowired
    public void setFixerService(FixerService fixerService) {

        this.fixerService = fixerService;
    }

    public String calculateProfit(String date, Double amount) throws IOException {

        String today = new SimpleDateFormat("yyyy-MM-dd").format( new Date() );
        String pastDate = date != null ? date : today;

        Currency presentCurrency;
        Currency pastCurrency;

        try {
            presentCurrency = parseJson(today);
            pastCurrency = parseJson(pastDate);
        } catch (IOException e) {
            return "something wrong. Message " + e.getMessage();
        }

        BigDecimal pricePast = extractCurrency(pastCurrency);
        BigDecimal pricePresent = extractCurrency(presentCurrency);

        BigDecimal currencyDistinction = pricePresent.subtract(pricePast).subtract(SPREAD);

        BigDecimal totalAmount = currencyDistinction.multiply(new BigDecimal(amount));

        return formatResponse(totalAmount);
    }

    private Currency parseJson(String stringDate) throws IOException{

        Gson g = new Gson();
        return g.fromJson(fixerService.getCurrencyAtDate(stringDate), Currency.class);
    }

    private BigDecimal extractCurrency(Currency currency) {

        if (currency.getRates() == null) {
            throw new RuntimeException("error at Fixer.io's json"); //подумать, как два разных эксепшна одним видом передавать
        } else {
            return currency.getRates().get("RUB").setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    private String formatResponse(BigDecimal totalAmount) {

        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            return "Ваша прибыль составила бы " + totalAmount + " руб.";
        } else if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            return "Ваш убыток составил бы " + totalAmount.negate() + " руб.";
        } else {
            return "Вы не заработали ничего. Но не грустите, ведь Вы ничего и не потеряли:)";
        }
    }
}
