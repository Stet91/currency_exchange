package com.fundcount.currency.service;

import com.fundcount.currency.TestConfig;
import com.fundcount.currency.exceptions.InfoException;
import com.fundcount.currency.serviceImpl.AccountingServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class AccountingServiceTest {

    private AccountingServiceImpl accountingService;

    @Autowired
    public void setAccountingService(AccountingServiceImpl accountingService) {
        this.accountingService = accountingService;
    }

    @Test
    public void calculateProfitTest() {
        String firstDate = "2012-05-05";
        String secondDate = "2019-03-31";
        String amount = "100.0";

        String expectResult = "Ваша прибыль составила бы 3411.00 руб.";

        String actualResult = accountingService.calculateProfitBetweenTwoDates(firstDate, secondDate, amount);

        Assert.assertEquals(expectResult, actualResult);
    }

    @Test(expected = InfoException.class)
    public void calculateProfitWithInvalidDatesTest() {
        String firstDate = "2012/05/05";
        String secondDate = "2019-03-31";
        String amount = "100.0";

        accountingService.calculateProfitBetweenTwoDates(firstDate, secondDate, amount);

    }

    @Test(expected = InfoException.class)
    public void calculateProfitWithoutDatesTest() {
        String firstDate = "";
        String secondDate = "2019-03-31";
        String amount = "100.0";

        accountingService.calculateProfitBetweenTwoDates(firstDate, secondDate, amount);

    }

    @Test(expected = InfoException.class)
    public void calculateProfitWithInvalidAmountFormatTest() {
        String firstDate = "2012-05-05";
        String secondDate = "2019-03-31";
        String amount = "one hundred";

        accountingService.calculateProfitBetweenTwoDates(firstDate, secondDate, amount);
    }

    @Test(expected = InfoException.class)
    public void calculateProfitWithoutAmountFormatTest() {
        String firstDate = "2012-05-05";
        String secondDate = "2019-03-31";
        String amount = "";

        accountingService.calculateProfitBetweenTwoDates(firstDate, secondDate, amount);
    }
}
