package ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.DefaultQuestionChecker;

import java.util.List;

public class DefaultSingleChoiceQuestionChecker implements DefaultQuestionChecker<SingleChoiceQuestion> {

    @Override
    public boolean check(SingleChoiceQuestion question) {
        List<Choice> choices = question.getChoices();
        for (Choice choice : choices) {
            if (choice.isMarked() != choice.isCorrectIt()) {
                return false;
            }
        }
        return true;
    }

}