package ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker;

import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.impl.DefaultMissingWordQuestionChecker;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.impl.DefaultMultiChoiceQuestionChecker;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.impl.DefaultSingleChoiceQuestionChecker;

public class DefaultQuestionCheckerFactory {

    public DefaultQuestionChecker getQuestionChecker(Question question) {
        String className = question.getClass().getName();

        if (className.equals(SingleChoiceQuestion.class.getName()))
            return new DefaultSingleChoiceQuestionChecker();

        if (className.equals(MultiChoiceQuestion.class.getName()))
            return new DefaultMultiChoiceQuestionChecker();

        if (className.equals(MissingWordQuestion.class.getName()))
            return new DefaultMissingWordQuestionChecker();

        return null;
    }

}
