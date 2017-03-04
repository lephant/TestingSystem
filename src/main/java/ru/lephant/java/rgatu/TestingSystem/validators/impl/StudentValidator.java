package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.studentvalidators.StudentFioValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.studentvalidators.StudentGroupValidator;

public class StudentValidator implements Validator<Student> {

    private StudentFioValidator studentFioValidator = new StudentFioValidator();
    private StudentGroupValidator studentGroupValidator = new StudentGroupValidator();


    @Override
    public boolean validate(Student student) {
        if (!studentFioValidator.validate(student)) return false;
        if (!studentGroupValidator.validate(student)) return false;
        return true;
    }

}