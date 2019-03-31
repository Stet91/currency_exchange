package com.fundcount.currency.controller;

import com.fundcount.currency.exceptions.InfoException;
import com.fundcount.currency.serviceImpl.AccountingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class GreetingController {

    @Autowired
    private AccountingServiceImpl accountingService;

    @GetMapping
    public String main() {
        return "main";
    }

    @PostMapping
    public String main(@RequestParam String date, @RequestParam String amount, Map<String, Object> model) {

        String message;
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try {
            message = accountingService.calculateProfitBetweenTwoDates(date, today, amount);
        } catch (InfoException e) {
            message = e.getMessage();
        }

        model.put("result", message);

        return "main";
    }
}