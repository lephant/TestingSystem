package ru.lephant.java.rgatu.TestingSystem.dao.services.impl;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dao.services.AbstractCRUDDAOService;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

public class StudentDAOService extends AbstractCRUDDAOService<Student> {

    @Override
    public Student getByPK(Serializable pk) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Student student = (Student) session
                .createCriteria(Student.class)
                .add(Restrictions.idEq(pk))
                .uniqueResult();
        session.close();
        return student;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> getList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Student> students = session.createCriteria(Student.class).list();
        session.close();
        return students;
    }
}