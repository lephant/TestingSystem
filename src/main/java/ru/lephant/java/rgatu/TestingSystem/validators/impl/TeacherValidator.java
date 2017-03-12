package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.teachervalidators.TeacherFioValidator;

public class TeacherValidator implements Validator<Teacher> {

    private TeacherFioValidator teacherFioValidator = new TeacherFioValidator();


    @Override
    public boolean validate(Teacher teacher) {
        if (!teacherFioValidator.validate(teacher)) {
            DialogFactory.createValidationErrorAlert(teacherFioValidator.getMessage()).show();
            return false;
        }
        return true;
    }

    @Override
    public String getMessage() {
        return "Ошибка валидации преподавателя!";
    }

}