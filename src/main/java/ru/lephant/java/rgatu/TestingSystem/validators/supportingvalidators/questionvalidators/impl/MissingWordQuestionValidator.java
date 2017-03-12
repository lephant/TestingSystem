package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.PossibleAnswerValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.AbstractQuestionValidator;

public class MissingWordQuestionValidator extends AbstractQuestionValidator<MissingWordQuestion> {

    private PossibleAnswerValidator possibleAnswerValidator = new PossibleAnswerValidator();


    @Override
    public boolean validate(MissingWordQuestion question) {
        if (!super.validate(question)) return false;

        if (question.getPossibleAnswers().size() < 1) {
            message = "В вопросе N%d должен быть хотя бы 1 вариант ответа!";
            return false;
        }

        for (MissingPossibleAnswer missingPossibleAnswer : question.getPossibleAnswers()) {
            if (!possibleAnswerValidator.validate(missingPossibleAnswer)) {
                message = possibleAnswerValidator.getMessage();
                return false;
            }
        }

        return true;
    }

}