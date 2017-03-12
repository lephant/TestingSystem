package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.testvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class TestNameValidator implements Validator<Test> {

    @Override
    public boolean validate(Test test) {
        String testName = test.getName();
        if (testName == null) return false;
        if (testName.trim().length() < 1) return false;
        if (testName.length() > 128) return false;
        return true;
    }

    @Override
    public String getMessage() {
        return "Название теста должно быть от 1 до 128 символов!";
    }

}