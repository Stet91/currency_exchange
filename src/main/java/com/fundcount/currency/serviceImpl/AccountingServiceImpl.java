package com.fundcount.currency.serviceImpl;

import com.fundcount.currency.domain.CurrencyDto;
import com.fundcount.currency.exceptions.InfoException;
import com.fundcount.currency.service.AccountingService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountingServiceImpl implements AccountingService {

    private static BigDecimal SPREAD = new BigDecimal("0.64");

    @Autowired
    private FixerServiceImpl fixerService;

    public String calculateProfitBetweenTwoDates(String firstDate, String secondDate, String amount) {

        validateDate(firstDate, secondDate);
        BigDecimal quantity = validateAndGetAmount(amount);

        CurrencyDto secondCurrencyDto;
        CurrencyDto firstCurrencyDto;

        try {
            firstCurrencyDto = getCurrencyFromFixer(firstDate);
            secondCurrencyDto = getCurrencyFromFixer(secondDate);
        } catch (IOException e) {
            return "something wrong. Message " + e.getMessage();
        }

        BigDecimal firstPrice = extractCurrency(firstCurrencyDto);
        BigDecimal secondPrice = extractCurrency(secondCurrencyDto);

        BigDecimal currencyDistinction = secondPrice.subtract(firstPrice).subtract(SPREAD);

        // округлено до 2 знаков после запятой из-за того, что клиент может купить не целое число, а так же дробное
        BigDecimal totalAmount = currencyDistinction.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP);

        return formatResponse(totalAmount);
    }

    private void validateDate(String firstDate, String secondDate) {

        if ((firstDate.length() == 0) || (secondDate.length() == 0)) {
            throw new InfoException("date is empty");
        }

        Pattern pattern = Pattern.compile("^([0-9]{4})-([0-9]{2})-([0-9]{2})$");
        Matcher firstMatcher = pattern.matcher(firstDate);
        Matcher secondMatcher = pattern.matcher(secondDate);

        if (!firstMatcher.find() || !secondMatcher.find()) {
            throw new InfoException("incorrect date format");
        }
    }

    private BigDecimal validateAndGetAmount(String amount) {

        if (amount.length() == 0) {
            throw new InfoException("amount is empty");
        }

        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            throw new InfoException("incorrect amount format");
        }
    }

    private CurrencyDto getCurrencyFromFixer(String stringDate) throws IOException {

        Gson gson = new Gson();
        return gson.fromJson(fixerService.getCurrencyAtDate(stringDate), CurrencyDto.class);
    }

    private BigDecimal extractCurrency(CurrencyDto currencyDto) {

        if (currencyDto.getRates() == null) {
            throw new InfoException("error at Fixer.io's json");
        } else {
            return currencyDto.getRates().get("RUB").setScale(2, BigDecimal.ROUND_HALF_UP);
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
