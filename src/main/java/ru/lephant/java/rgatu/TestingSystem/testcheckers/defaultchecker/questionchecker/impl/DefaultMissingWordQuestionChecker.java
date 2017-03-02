package ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.DefaultQuestionChecker;

public class DefaultMissingWordQuestionChecker implements DefaultQuestionChecker<MissingWordQuestion> {

    @Override
    public boolean check(MissingWordQuestion question) {
        if (question.getAnswer() == null) return false;
        String answer = question.getAnswer().trim().toLowerCase();
        for (MissingPossibleAnswer missingPossibleAnswer : question.getPossibleAnswers()) {
            String possibleAnswer = missingPossibleAnswer.getText().trim().toLowerCase();
            if (possibleAnswer.equals(answer)) {
                return true;
            }
        }
        return false;
    }

}