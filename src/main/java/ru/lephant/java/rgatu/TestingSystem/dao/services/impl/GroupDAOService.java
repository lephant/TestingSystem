package ru.lephant.java.rgatu.TestingSystem.dao.services.impl;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dao.services.AbstractCRUDDAOService;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;

public class GroupDAOService extends AbstractCRUDDAOService<Group> {

    @Override
    public Group getByPK(Serializable pk) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Group group = (Group) session
                .createCriteria(Group.class)
                .add(Restrictions.idEq(pk))
                .uniqueResult();
        session.close();
        return group;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> getList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Group> groups = session.createCriteria(Group.class).list();
        session.close();
        return groups;
    }
}