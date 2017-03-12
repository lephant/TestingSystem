package ru.lephant.java.rgatu.TestingSystem.dao.services.impl;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.entities.User;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

public class UserDAOService {

    public boolean checkUserByUsernameAndPassword(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User  user = (User) session.createCriteria(User.class)
                .add(Restrictions.eq("username", username))
                .add(Restrictions.eq("password", password))
                .add(Restrictions.eq("enabled", true))
                .setMaxResults(1)
                .uniqueResult();
        session.close();
        return user != null;
    }

}