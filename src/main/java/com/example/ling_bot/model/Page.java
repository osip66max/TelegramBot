package com.example.ling_bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {
    private String quoteText;

    public String getQuoteText() {
        return quoteText;
    }
}
