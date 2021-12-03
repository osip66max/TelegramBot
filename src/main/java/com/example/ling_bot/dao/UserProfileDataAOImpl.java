package com.example.ling_bot.dao;

import com.example.ling_bot.model.UserProfileData;
import com.example.ling_bot.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
    public UserProfileData show(String chatId) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(UserProfileData.class, chatId);
    }
}
