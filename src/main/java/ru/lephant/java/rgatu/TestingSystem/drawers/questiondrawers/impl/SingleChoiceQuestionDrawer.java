package ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl.SingleChoiceContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.resolvers.ToggleGroupResolver;

public class SingleChoiceQuestionDrawer implements QuestionDrawer {

    private ToggleGroupResolver toggleGroupResolver;
    private ContextMenuDrawer<Choice, RadioButton> contextMenuDrawer;


    public SingleChoiceQuestionDrawer() {
        toggleGroupResolver = new ToggleGroupResolver();
        contextMenuDrawer = new SingleChoiceContextMenuDrawer();
    }


    @Override
    public void draw(Question question, VBox choiceBox, boolean isEditMode) {
        SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
        ToggleGroup toggleGroup = toggleGroupResolver.resolve(singleChoiceQuestion);
        for (Choice choice : singleChoiceQuestion.getChoices()) {
            RadioButton radioButton = new RadioButton(choice.getText());
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setWrapText(true);

            if (isEditMode) {
                radioButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        for (Choice choice : singleChoiceQuestion.getChoices()) {
                            choice.setCorrectIt(false);
                        }
                        choice.setCorrectIt(true);
                    }
                });
                contextMenuDrawer.drawContextMenuInOption(choice, radioButton, choiceBox, singleChoiceQuestion);
                radioButton.setSelected(choice.isCorrectIt());
            } else {
                radioButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        for (Choice choice : singleChoiceQuestion.getChoices()) {
                            choice.setMarked(false);
                        }
                        choice.setMarked(true);
                    }
                });
                radioButton.setSelected(choice.isMarked());
            }
            choiceBox.getChildren().add(radioButton);
        }
    }

}