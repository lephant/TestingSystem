package ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

import java.util.Optional;

public class MissingWordQuestionDrawer implements QuestionDrawer {

    @Override
    public void draw(Question question, VBox choiceBox, boolean isEditMode) {
        MissingWordQuestion missingWordQuestion = (MissingWordQuestion) question;
        if (isEditMode) {
            for (MissingPossibleAnswer possibleAnswer : missingWordQuestion.getPossibleAnswers()) {
                TextField possibleAnswerField = new TextField();
                possibleAnswerField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        possibleAnswer.setText(newValue);
                    }
                });
                possibleAnswerField.setText(possibleAnswer.getText());
                addContextMenuToOption(possibleAnswer, possibleAnswerField, choiceBox, missingWordQuestion);
                choiceBox.getChildren().add(possibleAnswerField);
            }
        } else {
            TextField answerField = new TextField();
            answerField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    missingWordQuestion.setAnswer(newValue);
                }
            });
            answerField.setText(missingWordQuestion.getAnswer());
            choiceBox.getChildren().add(answerField);
        }
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