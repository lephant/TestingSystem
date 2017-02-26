package ru.lephant.java.rgatu.TestingSystem.dao.services.impl;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dao.services.AbstractCRUDDAOService;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

public class SubjectDAOService extends AbstractCRUDDAOService<Subject> {

    @Override
    public Subject getByPK(Serializable pk) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Subject subject = (Subject) session
                .createCriteria(Subject.class)
                .add(Restrictions.idEq(pk))
                .uniqueResult();
        session.close();
        return subject;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Subject> getList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Subject> subjects = session.createCriteria(Subject.class).list();
        session.close();
        return subjects;
    }
}
