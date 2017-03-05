package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.possibleanswervalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class PossibleAnswerTextValidator implements Validator<MissingPossibleAnswer> {

    @Override
    public boolean validate(MissingPossibleAnswer missingPossibleAnswer) {
        String missingPossibleAnswerText = missingPossibleAnswer.getText();
        if (missingPossibleAnswerText.trim().length() < 1) return false;
        if (missingPossibleAnswerText.length() > 255) return false;
        return true;
    }

}