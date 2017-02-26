package ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers;

import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

public interface QuestionDrawer {

    void draw(Question question, VBox choiceBox, boolean isEditMode);

}
