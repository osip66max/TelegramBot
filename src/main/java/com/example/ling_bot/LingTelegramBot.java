package com.example.ling_bot;

import com.example.ling_bot.botapi.TelegramFacade;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LingTelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private final TelegramFacade telegramFacade;

    public LingTelegramBot(DefaultBotOptions botOptions, TelegramFacade telegramFacade) {
        super(botOptions);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void sendPhoto(String chatId, String imageCaption, String imagePath) {
        try(InputStream inputStream = FileUtils.openInputStream(new File(imagePath))) {
            InputFile image = new InputFile(inputStream, "img.png");
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(image);
            sendPhoto.setChatId(chatId);
            sendPhoto.setCaption(imageCaption);
            execute(sendPhoto);
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void sendDocument(String chatId, String caption, InputFile sendFile) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setCaption(caption);
        sendDocument.setDocument(sendFile);
        execute(sendDocument);
    }
}
