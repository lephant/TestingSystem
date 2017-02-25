package ru.lephant.java.rgatu.TestingSystem.resolvers;

import javafx.scene.control.ToggleGroup;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;

public class ToggleGroupResolver {

    public ToggleGroup resolve (SingleChoiceQuestion question) {
        ToggleGroup toggleGroup;

        if (question.getToggleGroup() != null) {
            toggleGroup = question.getToggleGroup();
        } else {
            toggleGroup = new ToggleGroup();
            question.setToggleGroup(toggleGroup);
        }

        return toggleGroup;
    }

}