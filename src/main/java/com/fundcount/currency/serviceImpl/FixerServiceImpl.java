package com.fundcount.currency.serviceImpl;

import com.fundcount.currency.service.FixerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class FixerServiceImpl implements FixerService {

    @Value("${fixer.token}")
    private String token;

    @Value("${currency.source.url}")
    private String sourceUrl;

    public String getCurrencyAtDate(String date) throws IOException {

        String url = sourceUrl + date + "?access_key=" + token;

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
