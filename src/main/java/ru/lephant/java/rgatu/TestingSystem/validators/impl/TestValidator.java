package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.AbstractQuestionValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.questionvalidators.QuestionValidatorFactory;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.testvalidators.TestNameValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.testvalidators.TestSubjectValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.testvalidators.TestTeacherValidator;

import java.util.Formatter;

public class TestValidator implements Validator<Test> {

    private TestNameValidator testNameValidator = new TestNameValidator();
    private TestSubjectValidator testSubjectValidator = new TestSubjectValidator();
    private TestTeacherValidator testTeacherValidator = new TestTeacherValidator();
    private QuestionValidatorFactory questionValidatorFactory = new QuestionValidatorFactory();


    @Override
    public boolean validate(Test test) {
        if (!testNameValidator.validate(test)) {
            DialogFactory.createValidationErrorAlert(testNameValidator.getMessage()).show();
            return false;
        }
        if (!testSubjectValidator.validate(test)) {
            DialogFactory.createValidationErrorAlert(testSubjectValidator.getMessage()).show();
            return false;
        }
        if (!testTeacherValidator.validate(test)) {
            DialogFactory.createValidationErrorAlert(testTeacherValidator.getMessage()).show();
            return false;
        }

        if (test.getQuestions().size() < 1) {
            DialogFactory.createValidationErrorAlert("У теста должен быть хотя бы 1 вопрос!").show();
            return false;
        }

        for (int i = 0; i < test.getQuestions().size(); i++) {
            Question question = test.getQuestions().get(i);
            AbstractQuestionValidator questionValidator = questionValidatorFactory.getQuestionValidator(question);
            if (!questionValidator.validate(question)) {
                Formatter formatter = new Formatter();
                String message = formatter.format(questionValidator.getMessage(), i+1).toString();
                DialogFactory.createValidationErrorAlert(message).show();
                return false;
            }
        }

        return true;
    }

    @Override
    public String getMessage() {
        return "Ошибка валидации теста!";
    }

}