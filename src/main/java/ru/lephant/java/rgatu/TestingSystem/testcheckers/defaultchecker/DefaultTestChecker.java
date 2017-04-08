package ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker;

import javafx.util.Pair;
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
    public Pair<Double, Integer> check(Test test) {
        int receivedScores = 0;
        int totalScores = 0;

        for (Question question : test.getQuestions()) {
            totalScores += question.getValue();
            DefaultQuestionChecker defaultQuestionChecker = defaultQuestionCheckerFactory.getQuestionChecker(question);
            if (defaultQuestionChecker.check(question)) {
                receivedScores += question.getValue();
            }
        }

        double percent = (double) receivedScores / (double) totalScores;

        int mark;
        if (percent >= 91) mark = 5;
        else if (percent >= 76) mark = 4;
        else if (percent >= 60) mark = 3;
        else mark = 2;

        return new Pair<>(percent, mark);
    }
}
