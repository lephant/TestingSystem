package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.possibleanswervalidators.PossibleAnswerTextValidator;

public class PossibleAnswerValidator implements Validator<MissingPossibleAnswer> {

    private PossibleAnswerTextValidator possibleAnswerTextValidator = new PossibleAnswerTextValidator();

    private String message;


    @Override
    public boolean validate(MissingPossibleAnswer missingPossibleAnswer) {
        if (!possibleAnswerTextValidator.validate(missingPossibleAnswer)) {
            message = possibleAnswerTextValidator.getMessage();
            return false;
        }
        return true;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
