package com.example.ling_bot.botapi;

import com.example.ling_bot.LingTelegramBot;
import com.example.ling_bot.model.UserProfileData;
import com.example.ling_bot.cache.UserDataCache;
import com.example.ling_bot.service.MainMenuService;
import com.example.ling_bot.service.ReplyMessageService;
import com.example.ling_bot.utils.Emojis;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

@Component
@Slf4j
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;
    private final MainMenuService mainMenuService;
    private final LingTelegramBot lingTelegramBot;
    private final ReplyMessageService messageService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService,
                          @Lazy LingTelegramBot lingTelegramBot, ReplyMessageService messageService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.lingTelegramBot = lingTelegramBot;
        this.messageService = messageService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User:{}, chatId:{}, with data:{}",
                    update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(),
                    update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId:{}, chatId:{}, with text:{}",
                    message.getFrom().getUserName(),
                    message.getFrom().getId(),
                    message.getChatId(),
                    message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    @SneakyThrows
    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        Long userId = message.getFrom().getId();
        String localeTag = message.getFrom().getLanguageCode();
        String chatId = String.valueOf(message.getChatId());
        BotState botState;
        SendMessage replyMessage;

        String fillingProfile = messageService.getReplyText(localeTag, "reply.fillingProfile");
        String showUserProfile = messageService.getReplyText(localeTag, "reply.showUserProfile");
        String downloadUserProfile = messageService.getReplyText(localeTag, "reply.downloadUserProfile");
        String helpMenu = messageService.getReplyText(localeTag, "reply.helpMenu", Emojis.SOS);

        if ("/start".equals(inputMsg)) {
            botState = BotState.ASK_DESTINY;
            lingTelegramBot.sendPhoto(chatId, messageService.getReplyText(localeTag, "reply.hello", Emojis.MAGE), "static/images/img.png");
        } else if (fillingProfile.equals(inputMsg)) {
            botState = BotState.FILLING_PROFILE;
        } else if (showUserProfile.equals(inputMsg)) {
            botState = BotState.SHOW_USER_PROFILE;
        } else if (downloadUserProfile.equals(inputMsg)) {
            lingTelegramBot.sendDocument(chatId, messageService.getReplyText(localeTag, "reply.sendDocument"), getUsersProfile(userId, localeTag));
            botState = BotState.SHOW_USER_PROFILE;
        } else if (helpMenu.equals(inputMsg)) {
            botState = BotState.SHOW_HELP_MENU;
        } else {
            botState = userDataCache.getUsersCurrentBotState(userId);
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final String chatId = String.valueOf(buttonQuery.getMessage().getChatId());
        final Long userId = buttonQuery.getFrom().getId();
        final String localeTag = buttonQuery.getFrom().getLanguageCode();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, localeTag, messageService.getReplyText(localeTag, "reply.mainMenu"));

        if (buttonQuery.getData().equals("buttonYes")) {
            callBackAnswer = new SendMessage(chatId, messageService.getReplyText(localeTag, "reply.askName"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        } else if (buttonQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery(messageService.getReplyText(localeTag, "reply.return", Emojis.BACKWARD), false, buttonQuery);
        } else if (buttonQuery.getData().equals("buttonIWillThink")) {
            callBackAnswer = sendAnswerCallbackQuery(messageService.getReplyText(localeTag, "reply.notWork", Emojis.BACKWARD), true, buttonQuery);
        }

        else if (buttonQuery.getData().equals("buttonMan")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender(messageService.getReplyText(localeTag, "reply.man"));
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            callBackAnswer = messageService.getReplyMessage(chatId, localeTag,  "reply.askNumber", Emojis.STAR);
        } else if (buttonQuery.getData().equals("buttonWoman")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender(messageService.getReplyText(localeTag, "reply.woman"));
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            callBackAnswer = messageService.getReplyMessage(chatId, localeTag, "reply.askNumber", Emojis.STAR);

        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }

        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

    @SneakyThrows
    public InputFile getUsersProfile(Long userId, String localeTag) {
        UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
        File file = ResourceUtils.getFile("classpath:static/docs/users_profile.txt");

        try (FileWriter fw = new FileWriter(file.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(userProfileData.toString(localeTag, messageService));
        }

        return new InputFile(file);
    }
}
