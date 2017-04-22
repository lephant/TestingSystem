package ru.lephant.java.rgatu.TestingSystem.dao.services.impl;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dao.services.AbstractCRUDDAOService;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;

public class TeacherDAOService extends AbstractCRUDDAOService<Teacher> {

    @Override
    public Teacher getByPK(Serializable pk) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Teacher teacher = (Teacher) session
                .createCriteria(Teacher.class)
                .add(Restrictions.idEq(pk))
                .uniqueResult();
        session.close();
        return teacher;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Teacher> getList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Teacher> teachers = session.createCriteria(Teacher.class).list();
        session.close();
        return teachers;
    }
}