package ru.lephant.java.rgatu.TestingSystem.dao.services.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dao.services.AbstractCRUDDAOService;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;
import ru.lephant.java.rgatu.TestingSystem.util.HibernateUtil;
import ru.lephant.java.rgatu.TestingSystem.searchcriteries.StudentResultsSearchCriteria;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class StudentResultsDAOService extends AbstractCRUDDAOService<TestOfStudent> {

    @Override
    public TestOfStudent getByPK(Serializable pk) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        TestOfStudent testOfStudent = (TestOfStudent) session
                .createCriteria(TestOfStudent.class)
                .add(Restrictions.idEq(pk))
                .uniqueResult();
        session.close();
        return testOfStudent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TestOfStudent> getList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<TestOfStudent> testOfStudents = session.createCriteria(TestOfStudent.class).list();
        session.close();
        return testOfStudents;
    }

    @SuppressWarnings("unchecked")
    public List<TestOfStudent> getResultsOfStudentBySubject(Student student, Subject subject) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<TestOfStudent> testOfStudentList = session.createCriteria(TestOfStudent.class)
                .add(Restrictions.eq("student", student))
                .createAlias("test", "test")
                .add(Restrictions.eq("test.subject", subject))
                .list();
        session.close();
        return testOfStudentList;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getAllResultsOfStudent(Student student) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TestOfStudent.class);
        criteria.add(Restrictions.eq("student", student));
        criteria.createAlias("test", "test");
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.groupProperty("test.subject"));
        projectionList.add(Projections.avg("result"));
        criteria.setProjection(projectionList);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<TestOfStudent> getResultsByCriteria(StudentResultsSearchCriteria searchCriteria) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TestOfStudent.class);
        if (searchCriteria.getGroups() != null && !searchCriteria.getGroups().isEmpty()) {
            criteria.createAlias("student", "student");
            criteria.createAlias("student.group", "group");
            criteria.add(Restrictions.in("student.group", searchCriteria.getGroups()));
        }
        if (searchCriteria.getDate() != null) {
            Calendar from = Calendar.getInstance();
            from.setTime(searchCriteria.getDate());
            Calendar to = Calendar.getInstance();
            to.setTime(searchCriteria.getDate());
            to.add(Calendar.HOUR, 24);
            criteria.add(
                    Restrictions.and(
                            Restrictions.ge("dateAndTime", from.getTime()),
                            Restrictions.le("dateAndTime", to.getTime())
                    )
            );
        }
        List<TestOfStudent> testOfStudentList = criteria.list();
        session.close();
        return testOfStudentList;
    }
}