package ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl.SingleChoiceContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.OptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.resolvers.ToggleGroupResolver;

public class SingleChoiceOptionDrawer implements OptionDrawer {

    private ToggleGroupResolver toggleGroupResolver;
    private ContextMenuDrawer<Choice, RadioButton> contextMenuDrawer;


    public SingleChoiceOptionDrawer() {
        toggleGroupResolver = new ToggleGroupResolver();
        contextMenuDrawer = new SingleChoiceContextMenuDrawer();
    }


    @Override
    public void draw(Question question, VBox choiceBox) {
        SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
        ToggleGroup toggleGroup = toggleGroupResolver.resolve(singleChoiceQuestion);
        Choice choice = new Choice(question, "Новый вариант", false);
        singleChoiceQuestion.getChoices().add(choice);

        RadioButton radioButton = new RadioButton();
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setText("Новый вариант");
        radioButton.setSelected(false);

        radioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                choice.setCorrectIt(radioButton.isSelected());
            }
        });

        contextMenuDrawer.drawContextMenuInOption(choice, radioButton, choiceBox, singleChoiceQuestion);
        choiceBox.getChildren().add(radioButton);
    }

}