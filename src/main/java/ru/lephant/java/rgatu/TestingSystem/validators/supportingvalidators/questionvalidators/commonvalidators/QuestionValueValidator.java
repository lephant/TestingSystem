package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.commonvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class QuestionValueValidator implements Validator<Question> {

    @Override
    public boolean validate(Question question) {
        int questionValue = question.getValue();
        if (questionValue < 1) return false;
        if (questionValue > 1000) return false;
        return true;
    }

    @Override
    public String getMessage() {
        return "У вопроса N%d ценность должна быть от 1 до 1000!";
    }

}