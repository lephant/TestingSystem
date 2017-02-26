package ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl.MissingWordContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.OptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

public class MissingWordOptionDrawer implements OptionDrawer {

    private ContextMenuDrawer<MissingPossibleAnswer, TextField> contextMenuDrawer;


    public MissingWordOptionDrawer() {
        contextMenuDrawer = new MissingWordContextMenuDrawer();
    }


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

        contextMenuDrawer.drawContextMenuInOption(possibleAnswer, possibleAnswerField, choiceBox, missingWordQuestion);
        choiceBox.getChildren().add(possibleAnswerField);
    }

}