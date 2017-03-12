package ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.contextmenudrawers.ContextMenuDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.MultiChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

import java.util.Optional;

public class MultiChoiceContextMenuDrawer implements ContextMenuDrawer<Choice, CheckBox> {

    @Override
    public void drawContextMenuInOption(Choice choice, CheckBox checkBox, VBox choiceBox, Question question) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Редактировать");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = DialogFactory.createOptionEditDialog(choice.getText());
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
                ((MultiChoiceQuestion)question).getChoices().remove(choice);
                choiceBox.getChildren().remove(checkBox);
            }
        });
        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);
        checkBox.setContextMenu(contextMenu);
    }

}