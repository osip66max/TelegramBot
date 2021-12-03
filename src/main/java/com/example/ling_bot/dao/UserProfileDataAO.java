package com.example.ling_bot.dao;

import com.example.ling_bot.model.UserProfileData;

public interface UserProfileDataAO {
    void save(UserProfileData userProfileData);

    UserProfileData show(String chatId);
}
