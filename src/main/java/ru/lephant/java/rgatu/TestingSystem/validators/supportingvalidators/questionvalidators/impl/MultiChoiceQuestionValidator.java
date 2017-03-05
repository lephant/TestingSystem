package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.ChoiceValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.AbstractQuestionValidator;

public class MultiChoiceQuestionValidator extends AbstractQuestionValidator<MultiChoiceQuestion> {

    private ChoiceValidator choiceValidator = new ChoiceValidator();


    @Override
    public boolean validate(MultiChoiceQuestion question) {
        if (!super.validate(question)) return false;

        if (question.getChoices().size() < 1) return false;

        int isCorrectChoiceCount = 0;
        for (Choice choice : question.getChoices()) {
            if (!choiceValidator.validate(choice)) return false;
            if (choice.isCorrectIt()) isCorrectChoiceCount++;
        }
        if (isCorrectChoiceCount < 1) return false;

        return true;
    }

}