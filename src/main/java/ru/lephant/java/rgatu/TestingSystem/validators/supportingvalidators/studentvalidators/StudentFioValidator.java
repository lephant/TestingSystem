package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.studentvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class StudentFioValidator implements Validator<Student> {

    @Override
    public boolean validate(Student student) {
        String studentFio = student.getFio();
        if (studentFio == null) return false;
        if (studentFio.trim().length() < 1) return false;
        if (studentFio.length() > 255) return false;
        return true;
    }

    @Override
    public String getMessage() {
        return "ФИО студента должно быть от 1 до 255 символов!";
    }

}