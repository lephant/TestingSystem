package ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl.MultiChoiceContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.OptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

public class MultiChoiceOptionDrawer implements OptionDrawer {

    private ContextMenuDrawer<Choice, CheckBox> contextMenuDrawer;


    public MultiChoiceOptionDrawer() {
        contextMenuDrawer = new MultiChoiceContextMenuDrawer();
    }


    @Override
    public void draw(Question question, VBox choiceBox) {
        MultiChoiceQuestion multiChoiceQuestion = (MultiChoiceQuestion) question;
        Choice choice = new Choice(question, "Новый вариант", false);
        multiChoiceQuestion.getChoices().add(choice);

        CheckBox checkBox = new CheckBox();
        checkBox.setText("Новый вариант");
        checkBox.setSelected(false);

        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                choice.setCorrectIt(checkBox.isSelected());
            }
        });

        contextMenuDrawer.drawContextMenuInOption(choice, checkBox, choiceBox, multiChoiceQuestion);
        choiceBox.getChildren().add(checkBox);
    }

}