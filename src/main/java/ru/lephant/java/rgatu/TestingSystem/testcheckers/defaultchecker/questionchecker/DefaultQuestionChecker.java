package ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.questionchecker;

import ru.lephant.java.rgatu.TestingSystem.entities.Question;

public interface DefaultQuestionChecker<T extends Question> {

    boolean check(T question);

}