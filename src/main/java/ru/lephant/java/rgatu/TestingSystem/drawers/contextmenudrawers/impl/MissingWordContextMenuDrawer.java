package ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingPossibleAnswer;
import ru.lephant.java.rgatu.TestingSystem.entities.MissingWordQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

import java.util.Optional;

public class MissingWordContextMenuDrawer implements ContextMenuDrawer<MissingPossibleAnswer, TextField> {

    @Override
    public void drawContextMenuInOption(MissingPossibleAnswer possibleAnswer, TextField possibleAnswerField,
                                        VBox choiceBox, Question question) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editMenuItem = new MenuItem("Редактировать");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = DialogFactory.createOptionEditDialog(possibleAnswer.getText());
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
                ((MissingWordQuestion)question).getPossibleAnswers().remove(possibleAnswer);
                choiceBox.getChildren().remove(possibleAnswerField);
            }
        });

        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);
        possibleAnswerField.setContextMenu(contextMenu);
    }

}