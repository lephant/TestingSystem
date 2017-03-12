package ru.lephant.java.rgatu.TestingSystem.dao.services;

import java.io.Serializable;
import java.util.List;

public interface CRUDDaoService<T> {

    T getByPK(Serializable pk);

    List<T> getList();

    boolean delete(T object);

    void save(T object);

}