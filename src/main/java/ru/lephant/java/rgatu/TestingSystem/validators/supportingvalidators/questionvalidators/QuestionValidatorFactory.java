package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.impl.MissingWordQuestionValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.impl.MultiChoiceQuestionValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.impl.SingleChoiceQuestionValidator;

public class QuestionValidatorFactory {

    public AbstractQuestionValidator getQuestionValidator(Question question) {
        String className = question.getClass().getName();

        if (className.equals(SingleChoiceQuestion.class.getName()))
            return new SingleChoiceQuestionValidator();

        if (className.equals(MultiChoiceQuestion.class.getName()))
            return new MultiChoiceQuestionValidator();

        if (className.equals(MissingWordQuestion.class.getName()))
            return new MissingWordQuestionValidator();

        return null;
    }

}