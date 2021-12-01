package com.example.ling_bot.botapi.handlers.fillingprofile;

import com.example.ling_bot.botapi.BotState;
import com.example.ling_bot.botapi.InputMessageHandler;
import com.example.ling_bot.dao.UserProfileDataAO;
import com.example.ling_bot.model.UserProfileData;
import com.example.ling_bot.cache.UserDataCache;
import com.example.ling_bot.service.PredictionService;
import com.example.ling_bot.service.ReplyMessageService;
import com.example.ling_bot.utils.Emojis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessageService messagesService;
    private final PredictionService predictionService;
    private final UserProfileDataAO userProfileDataAO;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessageService messagesService, PredictionService predictionService, UserProfileDataAO userProfileDataAO) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.predictionService = predictionService;
        this.userProfileDataAO = userProfileDataAO;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        Long userId = inputMsg.getFrom().getId();
        String localeTag = inputMsg.getFrom().getLanguageCode();
        String chatId = String.valueOf(inputMsg.getChatId());

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, localeTag,"reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        }

        if (botState.equals(BotState.ASK_AGE)) {
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askAge");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_GENDER);
        }

        if (botState.equals(BotState.ASK_GENDER)) {
            try {
                profileData.setAge(Integer.parseInt(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askGender");
                replyToUser.setReplyMarkup(getGenderButtonsMarkup(localeTag));
            } catch (Exception e) {
                replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askAge");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_GENDER);
            }
        }

        if (botState.equals(BotState.ASK_NUMBER)) {
            profileData.setGender(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askNumber");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
        }

        if (botState.equals(BotState.ASK_COLOR)) {
            try {
                profileData.setNumber(Integer.parseInt(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askColor");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_MOVIE);
            } catch (NumberFormatException e) {
                replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askNumber");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            }
        }

        if (botState.equals(BotState.ASK_MOVIE)) {
            profileData.setColor(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askMovie");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SONG);
        }

        if (botState.equals(BotState.ASK_SONG)) {
            profileData.setMovie(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askSong");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setSong(usersAnswer);
            profileData.setChatId(chatId);
            profileData.setId(String.valueOf(userId));

            userProfileDataAO.save(profileData);

            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);

            String profileFilledMessage = messagesService.getReplyText(localeTag, "reply.profileFilled",
                    profileData.getName(), Emojis.SPARKLES);
            String predictionMessage = predictionService.getPrediction(localeTag);

            replyToUser = new SendMessage(chatId, String.format("%s%n%n%s %s", profileFilledMessage, Emojis.SCROLL, predictionMessage));
            replyToUser.setParseMode("HTML");
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }

    private InlineKeyboardMarkup getGenderButtonsMarkup(String localeTag) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonGenderMan = new InlineKeyboardButton();
        InlineKeyboardButton buttonGenderWoman = new InlineKeyboardButton();

        buttonGenderMan.setText(messagesService.getReplyText(localeTag, "reply.man"));
        buttonGenderWoman.setText(messagesService.getReplyText(localeTag, "reply.woman"));

        buttonGenderMan.setCallbackData("buttonMan");
        buttonGenderWoman.setCallbackData("buttonWoman");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonGenderMan);
        keyboardButtonsRow1.add(buttonGenderWoman);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
