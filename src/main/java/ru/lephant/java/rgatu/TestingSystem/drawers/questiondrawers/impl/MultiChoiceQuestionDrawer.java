package ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

import java.util.Optional;

public class MultiChoiceQuestionDrawer implements QuestionDrawer {

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
                addContextMenuToOption(choice, checkBox, choiceBox, multiChoiceQuestion);
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

    private void addContextMenuToOption(final Choice choice,
                                        final CheckBox checkBox,
                                        VBox choiceBox,
                                        MultiChoiceQuestion question) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Редактировать");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog(choice.getText());
                dialog.setTitle("Редактирование");
                dialog.setHeaderText(null);
                dialog.setContentText("Введите текст варианта ответа:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    choice.setText(result.get());
                    checkBox.setText(result.get());
                }
            }
        });
        MenuItem deleteMenuItem = new MenuItem("Удалить");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                question.getChoices().remove(choice);
                choiceBox.getChildren().remove(checkBox);
            }
        });
        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);
        checkBox.setContextMenu(contextMenu);
    }
}