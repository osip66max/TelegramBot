package com.example.ling_bot.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageService {
    private final MessageSource messageSource;

    public LocaleMessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String localeTag, String message) {
        Locale locale = getLocale(localeTag);
        return messageSource.getMessage(message, null, locale);
    }

    public String getMessage(String localeTag, String message,  Object... args) {
        Locale locale = getLocale(localeTag);
        return messageSource.getMessage(message, args, locale);
    }

    private Locale getLocale(String localeTag) {
        Locale locale;
        if (localeTag.equals("ru")) {
            locale = Locale.forLanguageTag("ru-RU");
        } else {
            locale = Locale.forLanguageTag("en-US");
        }
        return locale;
    }
}
