package com.example.ling_bot.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PredictionService {
    private final Random random = new Random();
    private final ReplyMessageService messagesService;

    public PredictionService(ReplyMessageService messagesService) {
        this.messagesService = messagesService;
    }

    public String getPrediction(String localeTag) {
        int predictionNumber = random.nextInt(5);
        String replyMessageProperties = String.format("%s%d", "reply.prediction", predictionNumber);
        return messagesService.getReplyText(localeTag, replyMessageProperties);
    }
}
