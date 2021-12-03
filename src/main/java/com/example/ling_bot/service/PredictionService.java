package com.example.ling_bot.service;

import com.example.ling_bot.model.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class PredictionService {
    /*private final Random random = new Random();
    private final ReplyMessageService messagesService;

    public PredictionService(ReplyMessageService messagesService) {
        this.messagesService = messagesService;
    }*/

    /*public String getPrediction(String localeTag) {
        int predictionNumber = random.nextInt(5);
        String replyMessageProperties = String.format("%s%d", "reply.prediction", predictionNumber);
        return messagesService.getReplyText(localeTag, replyMessageProperties);
    }*/

    public String getPrediction(String localeTag) {
        RestTemplate restTemplate = new RestTemplate();
        Page page = restTemplate.getForObject("http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=" +
                localeTag, Page.class);
        assert page != null;
        return page.getQuoteText();
    }
}
