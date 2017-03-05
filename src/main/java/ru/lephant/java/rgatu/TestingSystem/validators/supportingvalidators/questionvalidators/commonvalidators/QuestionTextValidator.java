package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.commonvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class QuestionTextValidator implements Validator<Question> {

    @Override
    public boolean validate(Question question) {
        String questionText = question.getText();
        if (questionText == null) return false;
        if (questionText.trim().length() < 1) return false;
        if (questionText.length() > 1024) return false;
        return true;
    }

}