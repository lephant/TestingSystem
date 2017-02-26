package ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Choice;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.SingleChoiceQuestion;
import ru.lephant.java.rgatu.TestingSystem.resolvers.ToggleGroupResolver;

import java.util.Optional;

public class SingleChoiceQuestionDrawer implements QuestionDrawer {

    private ToggleGroupResolver toggleGroupResolver;


    public SingleChoiceQuestionDrawer() {
        toggleGroupResolver = new ToggleGroupResolver();
    }


    @Override
    public void draw(Question question, VBox choiceBox, boolean isEditMode) {
        SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
        ToggleGroup toggleGroup = toggleGroupResolver.resolve(singleChoiceQuestion);
        for (Choice choice : singleChoiceQuestion.getChoices()) {
            RadioButton radioButton = new RadioButton(choice.getText());
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setWrapText(true);

            if (isEditMode) {
                radioButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        for (Choice choice : singleChoiceQuestion.getChoices()) {
                            choice.setCorrectIt(false);
                        }
                        choice.setCorrectIt(true);
                    }
                });
                addContextMenuToOption(choice, radioButton, choiceBox, singleChoiceQuestion);
                radioButton.setSelected(choice.isCorrectIt());
            } else {
                radioButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        for (Choice choice : singleChoiceQuestion.getChoices()) {
                            choice.setMarked(false);
                        }
                        choice.setMarked(true);
                    }
                });
                radioButton.setSelected(choice.isMarked());
            }
            choiceBox.getChildren().add(radioButton);
        }
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