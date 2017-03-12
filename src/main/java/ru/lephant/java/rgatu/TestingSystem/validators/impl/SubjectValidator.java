package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.subjectvalidators.SubjectNameValidator;

public class SubjectValidator implements Validator<Subject> {

    private SubjectNameValidator subjectNameValidator = new SubjectNameValidator();


    @Override
    public boolean validate(Subject subject) {
        if (!subjectNameValidator.validate(subject)) {
            DialogFactory.createValidationErrorAlert(subjectNameValidator.getMessage()).show();
            return false;
        }
        return true;
    }

    @Override
    public String getMessage() {
        return "Ошибка валидации предмета!";
    }

}