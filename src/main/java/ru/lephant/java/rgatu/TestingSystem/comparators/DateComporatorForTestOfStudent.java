package ru.lephant.java.rgatu.TestingSystem.comparators;

import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;

import java.util.Comparator;

public class DateComporatorForTestOfStudent implements Comparator<TestOfStudent> {

    @Override
    public int compare(TestOfStudent o1, TestOfStudent o2) {
        if (o1.getDateAndTime().getTime() > o2.getDateAndTime().getTime()) return 1;
        if (o1.getDateAndTime().getTime() < o2.getDateAndTime().getTime()) return -1;
        return 0;
    }

}