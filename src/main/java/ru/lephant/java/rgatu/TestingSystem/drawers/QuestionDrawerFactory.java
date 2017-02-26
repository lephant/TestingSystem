package ru.lephant.java.rgatu.TestingSystem.drawers;

import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl.MissingWordQuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl.MultiChoiceQuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl.SingleChoiceQuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;

public class QuestionDrawerFactory {

    public QuestionDrawer getQuestionDrawer(Question question) {
        String className = question.getClass().getName();

        if (className.equals(SingleChoiceQuestion.class.getName()))
            return new SingleChoiceQuestionDrawer();

        if (className.equals(MultiChoiceQuestion.class.getName()))
            return new MultiChoiceQuestionDrawer();

        if (className.equals(MissingWordQuestion.class.getName()))
            return new MissingWordQuestionDrawer();

        return null;
    }

}