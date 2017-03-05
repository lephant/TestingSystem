package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionoptionsvalidators.possibleanswervalidators.PossibleAnswerTextValidator;

public class PossibleAnswerValidator implements Validator<MissingPossibleAnswer> {

    private PossibleAnswerTextValidator possibleAnswerTextValidator = new PossibleAnswerTextValidator();


    @Override
    public boolean validate(MissingPossibleAnswer missingPossibleAnswer) {
        if (!possibleAnswerTextValidator.validate(missingPossibleAnswer)) return false;
        return true;
    }

}
