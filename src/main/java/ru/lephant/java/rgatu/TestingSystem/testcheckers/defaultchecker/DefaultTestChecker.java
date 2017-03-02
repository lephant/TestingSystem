package ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker;

import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.TestChecker;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.DefaultQuestionChecker;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.DefaultQuestionCheckerFactory;

public class DefaultTestChecker implements TestChecker {

    private DefaultQuestionCheckerFactory defaultQuestionCheckerFactory;


    public DefaultTestChecker() {
        defaultQuestionCheckerFactory = new DefaultQuestionCheckerFactory();
    }

    @Override
    public double check(Test test) {
        int receivedScores = 0;
        int totalScores = 0;

        for (Question question : test.getQuestions()) {
            totalScores += question.getValue();
            DefaultQuestionChecker defaultQuestionChecker = defaultQuestionCheckerFactory.getQuestionChecker(question);
            if (defaultQuestionChecker.check(question)) {
                receivedScores += question.getValue();
            }
        }

        return (double) receivedScores / (double) totalScores;
    }
}
