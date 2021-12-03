package com.example.ling_bot.service;

import com.example.ling_bot.model.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PredictionService {
    public String getPrediction(String localeTag) {
        RestTemplate restTemplate = new RestTemplate();
        Page page = restTemplate.getForObject("http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=" +
                localeTag, Page.class);
        assert page != null;
        return page.getQuoteText();
    }
}
