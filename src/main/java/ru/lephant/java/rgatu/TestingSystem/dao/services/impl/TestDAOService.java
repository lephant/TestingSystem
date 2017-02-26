package ru.lephant.java.rgatu.TestingSystem.dao.services.impl;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dao.services.AbstractCRUDDAOService;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

public class TestDAOService extends AbstractCRUDDAOService<Test> {

    @Override
    public Test getByPK(Serializable pk) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Test test = (Test) session
                .createCriteria(Test.class)
                .add(Restrictions.idEq(pk))
                .uniqueResult();
        Hibernate.initialize(test.getQuestions());
        session.close();
        return test;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Test> getList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Test> tests = session.createCriteria(Test.class).list();
        session.close();
        return tests;
    }
}
