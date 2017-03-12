package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.testvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class TestSubjectValidator implements Validator<Test> {

    @Override
    public boolean validate(Test test) {
        if (test.getSubject() == null) return false;
        return true;
    }

    @Override
    public String getMessage() {
        return "У теста должен быть указан предмет!";
    }

}