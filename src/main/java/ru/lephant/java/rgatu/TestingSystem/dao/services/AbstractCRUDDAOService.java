package ru.lephant.java.rgatu.TestingSystem.dao.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.lephant.java.rgatu.TestingSystem.util.HibernateUtil;

public abstract class AbstractCRUDDAOService<T> implements CRUDDaoService<T> {

    @Override
    public boolean delete(T object) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(object);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void save(T object) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(object);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}