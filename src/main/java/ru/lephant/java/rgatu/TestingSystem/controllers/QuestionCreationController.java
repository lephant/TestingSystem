package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.resolvers.CreationQuestionResolver;

import java.net.URL;
import java.util.ResourceBundle;

public class QuestionCreationController extends AbstractController {

    @FXML
    public ToggleGroup toggleGroup;

    @FXML
    public RadioButton singleChoiceQuestionRadioButton;

    @FXML
    public RadioButton multiChoiceQuestionRadioButton;

    @FXML
    public RadioButton missingWordQuestionRadioButton;

    private Stage currentStage;
    private Question createdQuestion;
    private CreationQuestionResolver creationQuestionResolver;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        singleChoiceQuestionRadioButton.setUserData(CreationQuestionResolver.SINGLE_CHOICE_QUESTION);
        multiChoiceQuestionRadioButton.setUserData(CreationQuestionResolver.MULTI_CHOICE_QUESTION);
        missingWordQuestionRadioButton.setUserData(CreationQuestionResolver.MISSING_WORD_QUESTION);

        creationQuestionResolver = new CreationQuestionResolver();
    }


    @FXML
    public void onCreateQuestionButtonClicked() {
        Question question = creationQuestionResolver.resolve(toggleGroup);
        if (question != null) {
            createdQuestion = question;
            currentStage.close();
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран ни один тип вопроса!");
            noSelectedItemAlert.show();
            createdQuestion = null;
        }
    }

    @FXML
    public void onCancelButtonClicked() {
        createdQuestion = null;
        currentStage.close();
    }


    public Question getCreatedQuestion() {
        return createdQuestion;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}