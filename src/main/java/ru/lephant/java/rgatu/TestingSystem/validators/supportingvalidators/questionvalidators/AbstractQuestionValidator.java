package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.commonvalidators.QuestionTextValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.commonvalidators.QuestionValueValidator;

public abstract class AbstractQuestionValidator<QUESTION extends Question> implements Validator<QUESTION> {

    private QuestionTextValidator questionTextValidator = new QuestionTextValidator();
    private QuestionValueValidator questionValueValidator = new QuestionValueValidator();


    @Override
    public boolean validate(QUESTION question) {
        if (!questionTextValidator.validate(question)) return false;
        if (!questionValueValidator.validate(question)) return false;
        return true;
    }

}