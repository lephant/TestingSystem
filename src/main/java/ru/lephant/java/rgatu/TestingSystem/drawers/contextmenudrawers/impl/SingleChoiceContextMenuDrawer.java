package ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;

import java.util.Optional;

public class SingleChoiceContextMenuDrawer implements ContextMenuDrawer<Choice, RadioButton> {

    @Override
    public void drawContextMenuInOption(Choice choice, RadioButton radioButton, VBox choiceBox, Question question) {
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
                    radioButton.setText(result.get());
                }
            }
        });
        MenuItem deleteMenuItem = new MenuItem("Удалить");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((SingleChoiceQuestion)question).getChoices().remove(choice);
                choiceBox.getChildren().remove(radioButton);
            }
        });
        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);
        radioButton.setContextMenu(contextMenu);
    }

}