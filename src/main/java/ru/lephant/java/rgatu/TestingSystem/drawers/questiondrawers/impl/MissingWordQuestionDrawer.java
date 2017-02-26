package ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl.MissingWordContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

public class MissingWordQuestionDrawer implements QuestionDrawer {

    private ContextMenuDrawer<MissingPossibleAnswer, TextField> contextMenuDrawer;


    public MissingWordQuestionDrawer() {
        contextMenuDrawer = new MissingWordContextMenuDrawer();
    }


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
                contextMenuDrawer.drawContextMenuInOption(possibleAnswer, possibleAnswerField, choiceBox, missingWordQuestion);
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

}