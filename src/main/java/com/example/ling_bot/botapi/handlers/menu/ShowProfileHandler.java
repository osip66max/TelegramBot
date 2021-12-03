package com.example.ling_bot.botapi.handlers.menu;

import com.example.ling_bot.botapi.BotState;
import com.example.ling_bot.botapi.InputMessageHandler;
import com.example.ling_bot.dao.UserProfileDataAOImpl;
import com.example.ling_bot.model.UserProfileData;
import com.example.ling_bot.cache.UserDataCache;
import com.example.ling_bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ShowProfileHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final UserProfileDataAOImpl userProfileDataAOImpl;
    private final ReplyMessageService messageService;

    public ShowProfileHandler(UserDataCache userDataCache, UserProfileDataAOImpl userProfileDataAOImpl,
                              ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.userProfileDataAOImpl = userProfileDataAOImpl;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        final Long userId = message.getFrom().getId();
        final String localeTag = message.getFrom().getLanguageCode();
        final UserProfileData profileData = userProfileDataAOImpl.show(String.valueOf(message.getChatId()));

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        return new SendMessage(String.valueOf(message.getChatId()),
                String.format("%s%n--------------------%n%s", messageService.getReplyText(localeTag, "reply.showProfile"), profileData.toString(localeTag, messageService)));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
