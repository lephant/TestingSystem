package ru.lephant.java.rgatu.TestingSystem.resolvers;

import javafx.scene.control.ToggleGroup;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;

public class CreationQuestionResolver {

    public static final String SINGLE_CHOICE_QUESTION = "singleChoiceQuestionRadioButton";
    public static final String MULTI_CHOICE_QUESTION = "multiChoiceQuestionRadioButton";
    public static final String MISSING_WORD_QUESTION = "missingWordQuestionRadioButton";

    public Question resolve(ToggleGroup toggleGroup) {
        Question question = null;
        String userData = toggleGroup.getSelectedToggle().getUserData().toString();

        switch (userData) {
            case SINGLE_CHOICE_QUESTION:
                question = new SingleChoiceQuestion();
                break;

            case MULTI_CHOICE_QUESTION:
                question = new MultiChoiceQuestion();
                break;

            case MISSING_WORD_QUESTION:
                question = new MissingWordQuestion();
                break;
        }

        return question;
    }

}