package ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.OptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.*;

import java.util.Optional;

public class MissingWordOptionDrawer implements OptionDrawer {

    @Override
    public void draw(Question question, VBox choiceBox) {
        MissingWordQuestion missingWordQuestion = (MissingWordQuestion) question;
        MissingPossibleAnswer possibleAnswer = new MissingPossibleAnswer(missingWordQuestion, "Новый вариант");
        missingWordQuestion.getPossibleAnswers().add(possibleAnswer);

        TextField possibleAnswerField = new TextField();
        possibleAnswerField.setText("Новый вариант");
        possibleAnswerField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                possibleAnswer.setText(newValue);
            }
        });

        addContextMenuToOption(possibleAnswer, possibleAnswerField, choiceBox, missingWordQuestion);
        choiceBox.getChildren().add(possibleAnswerField);
    }

    private void addContextMenuToOption(MissingPossibleAnswer possibleAnswer,
                                        TextField possibleAnswerField,
                                        VBox choiceBox,
                                        MissingWordQuestion question) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editMenuItem = new MenuItem("Редактировать");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog(possibleAnswer.getText());
                dialog.setTitle("Редактирование");
                dialog.setHeaderText(null);
                dialog.setContentText("Введите текст варианта ответа:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    possibleAnswer.setText(result.get());
                    possibleAnswerField.setText(result.get());
                }
            }
        });

        MenuItem deleteMenuItem = new MenuItem("Удалить");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                question.getPossibleAnswers().remove(possibleAnswer);
                choiceBox.getChildren().remove(possibleAnswerField);
            }
        });

        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);
        possibleAnswerField.setContextMenu(contextMenu);
    }
}