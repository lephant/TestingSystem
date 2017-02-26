package ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.OptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.resolvers.ToggleGroupResolver;

import java.util.Optional;

public class SingleChoiceOptionDrawer implements OptionDrawer {

    private ToggleGroupResolver toggleGroupResolver;


    public SingleChoiceOptionDrawer() {
        toggleGroupResolver = new ToggleGroupResolver();
    }


    @Override
    public void draw(Question question, VBox choiceBox) {
        SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
        ToggleGroup toggleGroup = toggleGroupResolver.resolve(singleChoiceQuestion);
        Choice choice = new Choice(question, "Новый вариант", false);
        singleChoiceQuestion.getChoices().add(choice);

        RadioButton radioButton = new RadioButton();
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setText("Новый вариант");
        radioButton.setSelected(false);

        radioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                choice.setCorrectIt(radioButton.isSelected());
            }
        });

        addContextMenuToOption(choice, radioButton, choiceBox, singleChoiceQuestion);
        choiceBox.getChildren().add(radioButton);
    }


    private void addContextMenuToOption(final Choice choice,
                                        final RadioButton radioButton,
                                        VBox choiceBox,
                                        SingleChoiceQuestion question) {
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
                question.getChoices().remove(choice);
                choiceBox.getChildren().remove(radioButton);
            }
        });
        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);
        radioButton.setContextMenu(contextMenu);
    }

}