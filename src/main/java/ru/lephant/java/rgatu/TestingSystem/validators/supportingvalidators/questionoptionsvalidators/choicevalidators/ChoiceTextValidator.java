package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.choicevalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class ChoiceTextValidator implements Validator<Choice> {

    @Override
    public boolean validate(Choice choice) {
        String choiceText = choice.getText();
        if (choiceText.trim().length() < 1) return false;
        if (choiceText.length() > 255) return false;
        return true;
    }

}