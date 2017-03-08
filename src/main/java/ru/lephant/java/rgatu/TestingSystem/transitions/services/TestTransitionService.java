package ru.lephant.java.rgatu.TestingSystem.transitions.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.lephant.java.rgatu.TestingSystem.controllers.QuestionCreationController;
import ru.lephant.java.rgatu.TestingSystem.controllers.TestEditingController;
import ru.lephant.java.rgatu.TestingSystem.controllers.TestExecutionController;
import ru.lephant.java.rgatu.TestingSystem.controllers.TestSelectionController;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.reference.ReferenceData;
import java.io.IOException;

public class TestTransitionService {

    public Stage createTestSelectionStage() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/test_selection.fxml"));
            Parent root = fxmlLoader.load();

            TestSelectionController testSelectionController = fxmlLoader.getController();
            testSelectionController.setCurrentStage(stage);
            testSelectionController.postInitialize();

            stage.setScene(new Scene(root));
            stage.setTitle("Система тестирования \"Degress\"");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setHeight(400D);
            stage.setWidth(420D);
            stage.setMinWidth(420D);
            stage.setMinHeight(400D);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage createTestEditingStage(Stage mainStage, Test test, RefreshableController parentController) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/test_editing.fxml"));
            Parent root = loader.load();

            TestEditingController testEditingController = loader.getController();
            testEditingController.setCurrentStage(stage);
            testEditingController.setParentController(parentController);
            testEditingController.setTest(test);
            testEditingController.postInitialize();
            testEditingController.showQuestion(0);

            stage.setScene(new Scene(root));
            stage.setTitle("Конструктор тестов");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setWidth(720D);
            stage.setHeight(620D);
            stage.setMinWidth(720D);
            stage.setMinHeight(620D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage createTestExecutionStage(Stage mainStage, Test test, Student student) {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/test_execution.fxml"));
            Parent root = fxmlLoader.load();

            TestExecutionController testExecutionController = fxmlLoader.getController();
            testExecutionController.setMainStage(mainStage);
            testExecutionController.setCurrentStage(stage);
            testExecutionController.setTest(test);
            testExecutionController.setStudent(student);
            testExecutionController.postInitialize();
            testExecutionController.showQuestion(0);

            stage.setScene(new Scene(root));
            stage.setTitle("Система тестирования \"Degress\"");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setWidth(700D);
            stage.setHeight(500D);
            stage.setMinWidth(700D);
            stage.setMinHeight(500D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pair<Stage, QuestionCreationController> createQuestionAddingStage(Stage parentStage) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/question_creation_window.fxml"));
            Parent root = loader.load();

            QuestionCreationController questionCreationController = loader.getController();
            questionCreationController.setCurrentStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Создание вопроса");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setHeight(180D);
            stage.setWidth(300D);
            stage.setResizable(false);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            return new Pair<>(stage, questionCreationController);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}