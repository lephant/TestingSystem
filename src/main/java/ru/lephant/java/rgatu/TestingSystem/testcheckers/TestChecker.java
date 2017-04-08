package ru.lephant.java.rgatu.TestingSystem.testcheckers;

import javafx.util.Pair;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;

public interface TestChecker {

    Pair<Double, Integer> check(Test test);

}