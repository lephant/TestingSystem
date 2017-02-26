package ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers;

import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

public interface OptionDrawer {

    void draw(Question question, VBox choiceBox);

}