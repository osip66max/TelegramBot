package com.example.ling_bot.cache;

import com.example.ling_bot.botapi.BotState;
import com.example.ling_bot.model.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(Long userId, BotState botState);

    BotState getUsersCurrentBotState(Long userId);

    UserProfileData getUserProfileData(Long userId);

    void saveUserProfileData(Long userId, UserProfileData userProfileData);
}
