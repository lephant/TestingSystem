package ru.lephant.java.rgatu.TestingSystem.validators;

public interface Validator<T> {

    boolean validate(T entity);

}