package com.example.ling_bot.dao;

import com.example.ling_bot.model.UserProfileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserProfileDataAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProfileDataAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(UserProfileData userProfileData) {
        String chatId = userProfileData.getChatId();
        update(chatId, userProfileData);
        jdbcTemplate.update("INSERT INTO UserProfileData(id, name, gender, color, movie, song, age, number, chatid) " +
                        "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM UserProfileData WHERE chatid=?)",
                userProfileData.getId(),
                userProfileData.getName(),
                userProfileData.getGender(),
                userProfileData.getColor(),
                userProfileData.getMovie(),
                userProfileData.getSong(),
                userProfileData.getAge(),
                userProfileData.getNumber(),
                chatId, chatId);
    }

    public void update(String chatId, UserProfileData userProfileData) {
        jdbcTemplate.update("UPDATE UserProfileData SET id=?, name=?, gender=?, color=?, movie=?, song=?, age=?, " +
                "number=? WHERE chatid=?",
                userProfileData.getId(),
                userProfileData.getName(),
                userProfileData.getGender(),
                userProfileData.getColor(),
                userProfileData.getMovie(),
                userProfileData.getSong(),
                userProfileData.getAge(),
                userProfileData.getNumber(), chatId);
    }

    public UserProfileData show(String chatId) {
        return jdbcTemplate.query("SELECT * FROM UserProfileData WHERE chatid=?",
                new BeanPropertyRowMapper<>(UserProfileData.class), new Object[]{chatId}).stream().findAny().orElse(null);
    }
}
