package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.ChoiceValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.AbstractQuestionValidator;

public class SingleChoiceQuestionValidator extends AbstractQuestionValidator<SingleChoiceQuestion> {

    private ChoiceValidator choiceValidator = new ChoiceValidator();


    @Override
    public boolean validate(SingleChoiceQuestion question) {
        if (!super.validate(question)) return false;

        if (question.getChoices().size() < 1) return false;

        int isCorrectChoiceCount = 0;
        for (Choice choice : question.getChoices()) {
            if (!choiceValidator.validate(choice)) return false;
            if (choice.isCorrectIt()) isCorrectChoiceCount++;
        }
        if (isCorrectChoiceCount != 1) return false;

        return true;
    }

}