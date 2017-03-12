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

        if (question.getChoices().size() < 2) {
            message = "Вопрос N%d должен содержать минимум 2 варианта ответа!";
            return false;
        }

        int isCorrectChoiceCount = 0;
        for (Choice choice : question.getChoices()) {
            if (!choiceValidator.validate(choice)) {
                message = choiceValidator.getMessage();
                return false;
            }
            if (choice.isCorrectIt()) isCorrectChoiceCount++;
        }
        if (isCorrectChoiceCount < 1) {
            message = "Вопрос N%d должен содержать 1 или более правильных ответов!";
            return false;
        }

        return true;
    }

}