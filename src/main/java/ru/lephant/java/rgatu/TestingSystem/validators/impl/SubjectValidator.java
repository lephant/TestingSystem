package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.subjectvalidators.SubjectNameValidator;

public class SubjectValidator implements Validator<Subject> {

    private SubjectNameValidator subjectNameValidator = new SubjectNameValidator();


    @Override
    public boolean validate(Subject subject) {
        if (!subjectNameValidator.validate(subject)) return false;
        return true;
    }

}