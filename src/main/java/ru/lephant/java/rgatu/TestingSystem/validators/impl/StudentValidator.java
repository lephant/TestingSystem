package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.studentvalidators.StudentFioValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.studentvalidators.StudentGroupValidator;

public class StudentValidator implements Validator<Student> {

    private StudentFioValidator studentFioValidator = new StudentFioValidator();
    private StudentGroupValidator studentGroupValidator = new StudentGroupValidator();


    @Override
    public boolean validate(Student student) {
        if (!studentFioValidator.validate(student)) {
            DialogFactory.createValidationErrorAlert(studentFioValidator.getMessage()).show();
            return false;
        }
        if (!studentGroupValidator.validate(student)) {
            DialogFactory.createValidationErrorAlert(studentGroupValidator.getMessage()).show();
            return false;
        }
        return true;
    }

    @Override
    public String getMessage() {
        return "Ошибка валидации студента!";
    }

}