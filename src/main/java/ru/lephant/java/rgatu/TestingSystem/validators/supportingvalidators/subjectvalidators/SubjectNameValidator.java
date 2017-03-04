package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.subjectvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class SubjectNameValidator implements Validator<Subject> {

    @Override
    public boolean validate(Subject subject) {
        String subjectName = subject.getName();
        if (subjectName.trim().length() < 1) return false;
        if (subjectName.length() > 128) return false;
        return true;
    }

}