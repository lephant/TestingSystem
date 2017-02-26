package ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl.MultiChoiceContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

public class MultiChoiceQuestionDrawer implements QuestionDrawer {

    private ContextMenuDrawer<Choice, CheckBox> contextMenuDrawer;


    public MultiChoiceQuestionDrawer() {
        contextMenuDrawer = new MultiChoiceContextMenuDrawer();
    }


    @Override
    public void draw(Question question, VBox choiceBox, boolean isEditMode) {
        MultiChoiceQuestion multiChoiceQuestion = (MultiChoiceQuestion) question;
        for (Choice choice : multiChoiceQuestion.getChoices()) {
            CheckBox checkBox = new CheckBox(choice.getText());
            checkBox.setWrapText(true);
            if (isEditMode) {
                checkBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        choice.setCorrectIt(checkBox.isSelected());
                    }
                });
                checkBox.setSelected(choice.isCorrectIt());
                contextMenuDrawer.drawContextMenuInOption(choice, checkBox, choiceBox, multiChoiceQuestion);
            } else {
                checkBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        choice.setMarked(checkBox.isSelected());
                    }
                });
                checkBox.setSelected(choice.isMarked());
            }
            choiceBox.getChildren().add(checkBox);
        }
    }

}