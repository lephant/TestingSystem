package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.choicevalidators.ChoiceTextValidator;

public class ChoiceValidator implements Validator<Choice> {

    private ChoiceTextValidator choiceTextValidator = new ChoiceTextValidator();


    @Override
    public boolean validate(Choice choice) {
        if (!choiceTextValidator.validate(choice)) return false;
        return true;
    }

}