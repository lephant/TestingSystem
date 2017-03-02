package ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker.DefaultQuestionChecker;

import java.util.List;

public class DefaultMultiChoiceQuestionChecker implements DefaultQuestionChecker<MultiChoiceQuestion> {

    @Override
    public boolean check(MultiChoiceQuestion question) {
        List<Choice> choices = question.getChoices();
        for (Choice choice : choices) {
            if (choice.isMarked() != choice.isCorrectIt()) {
                return false;
            }
        }
        return true;
    }

}