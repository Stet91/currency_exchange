package com.fundcount.currency;

import com.fundcount.currency.service.AccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
public class GreetingController {

    private AccountingService accountingService;

    @Autowired
    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @GetMapping
    public String main() {
        return "main";
    }

    @PostMapping
    public String main(@RequestParam String date, @RequestParam Double amount, Map<String, Object> model) throws IOException {

        String result = accountingService.calculateProfit(date, amount);
        model.put("result", result);

        return "main";
    }
}