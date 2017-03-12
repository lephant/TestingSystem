package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.choicevalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class ChoiceTextValidator implements Validator<Choice> {

    @Override
    public boolean validate(Choice choice) {
        String choiceText = choice.getText();
        if (choiceText == null) return false;
        if (choiceText.trim().length() < 1) return false;
        if (choiceText.length() > 255) return false;
        return true;
    }

    @Override
    public String getMessage() {
        return "В вопросе N%d текст варианта должен быть от 1 до 255 символов!";
    }

}