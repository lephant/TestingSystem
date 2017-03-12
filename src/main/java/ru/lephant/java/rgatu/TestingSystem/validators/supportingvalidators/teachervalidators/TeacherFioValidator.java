package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.teachervalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class TeacherFioValidator implements Validator<Teacher> {

    @Override
    public boolean validate(Teacher teacher) {
        String teacherFio = teacher.getFio();
        if (teacherFio == null) return false;
        if (teacherFio.trim().length() < 1) return false;
        if (teacherFio.length() > 255) return false;
        return true;
    }

    @Override
    public String getMessage() {
        return "ФИО преподавателя должно быть от 1 до 255 символов!";
    }

}