package com.example.ling_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {
    private final LocaleMessageService localeMessageService;

    public ReplyMessageService(LocaleMessageService messageService) {
        this.localeMessageService = messageService;
    }

    public SendMessage getReplyMessage(String chatId, String localeTag, String replyMessage) {
        return new SendMessage(chatId, localeMessageService.getMessage(localeTag, replyMessage));
    }

    public SendMessage getReplyMessage(String chatId, String localeTag, String replyMessage, Object... args) {
        return new SendMessage(chatId, localeMessageService.getMessage(localeTag, replyMessage, args));
    }

    public String getReplyText(String localeTag, String replyText) {
        return localeMessageService.getMessage(localeTag, replyText);
    }

    public String getReplyText(String localeTag, String replyText, Object... args) {
        return localeMessageService.getMessage(localeTag, replyText, args);
    }
}
