package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.AbstractQuestionValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.QuestionValidatorFactory;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.testvalidators.TestNameValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.testvalidators.TestSubjectValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.testvalidators.TestTeacherValidator;

public class TestValidator implements Validator<Test> {

    private TestNameValidator testNameValidator = new TestNameValidator();
    private TestSubjectValidator testSubjectValidator = new TestSubjectValidator();
    private TestTeacherValidator testTeacherValidator = new TestTeacherValidator();
    private QuestionValidatorFactory questionValidatorFactory = new QuestionValidatorFactory();

    @Override
    public boolean validate(Test test) {
        if (!testNameValidator.validate(test)) return false;
        if (!testSubjectValidator.validate(test)) return false;
        if (!testTeacherValidator.validate(test)) return false;

        if (test.getQuestions().size() < 1) return false;

        for (Question question : test.getQuestions()) {
            AbstractQuestionValidator questionValidator = questionValidatorFactory.getQuestionValidator(question);
            if (!questionValidator.validate(question)) return false;
        }

        return true;
    }

}