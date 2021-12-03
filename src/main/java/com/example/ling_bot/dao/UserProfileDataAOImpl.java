package com.example.ling_bot.dao;

import com.example.ling_bot.model.UserProfileData;
import com.example.ling_bot.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserProfileDataAOImpl implements UserProfileDataAO {
    @Override
    public void save(UserProfileData userProfileData) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.saveOrUpdate(userProfileData);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(String chatId, UserProfileData userProfileData) {

    }

    @Override
    public UserProfileData show(String chatId) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(UserProfileData.class, chatId);
    }
    /*private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProfileDataAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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

    @Override
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

    @Override
    public UserProfileData show(String chatId) {
        return jdbcTemplate.query("SELECT * FROM UserProfileData WHERE chatid=?",
                new BeanPropertyRowMapper<>(UserProfileData.class), new Object[]{chatId}).stream().findAny().orElse(null);
    }*/


}
