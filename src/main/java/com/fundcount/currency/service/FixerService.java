package com.fundcount.currency.service;

import java.io.IOException;

public interface FixerService {

    String getCurrencyAtDate(String date) throws IOException;
}
