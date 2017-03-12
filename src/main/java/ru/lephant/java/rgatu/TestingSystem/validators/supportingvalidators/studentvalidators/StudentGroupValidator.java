package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.studentvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class StudentGroupValidator implements Validator<Student> {

    @Override
    public boolean validate(Student student) {
        if (student.getGroup() == null) return false;
        return true;
    }

    @Override
    public String getMessage() {
        return "У студента должна быть указана группа!";
    }

}
