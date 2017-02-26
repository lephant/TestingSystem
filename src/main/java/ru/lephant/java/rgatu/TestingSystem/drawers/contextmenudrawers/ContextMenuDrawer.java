package ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers;

import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

public interface ContextMenuDrawer<MODEL, VIEW> {

    void drawContextMenuInOption(MODEL model, VIEW view,
                                 VBox choiceBox, Question question);

}