package ru.lephant.java.rgatu.TestingSystem.drawers;

import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.OptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.impl.MissingWordOptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.impl.MultiChoiceOptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.impl.SingleChoiceOptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;

public class OptionDrawerFactory {

    public OptionDrawer getOptionDrawer(Question question) {
        String className = question.getClass().getName();

        if (className.equals(SingleChoiceQuestion.class.getName()))
            return new SingleChoiceOptionDrawer();

        if (className.equals(MultiChoiceQuestion.class.getName()))
            return new MultiChoiceOptionDrawer();

        if (className.equals(MissingWordQuestion.class.getName()))
            return new MissingWordOptionDrawer();

        return null;
    }

}