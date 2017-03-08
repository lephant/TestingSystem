package ru.lephant.java.rgatu.TestingSystem.transitions.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.controllers.*;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.reference.ReferenceData;

import java.io.IOException;

public class StudentTransitionService {

    public Stage createStudentSelectionStage(Stage mainStage, Test test) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_selection.fxml"));
            Parent root = loader.load();

            StudentSelectionController studentSelectionController = loader.getController();
            studentSelectionController.setMainStage(mainStage);
            studentSelectionController.setCurrentStage(stage);
            studentSelectionController.setTest(test);

            stage.setScene(new Scene(root));
            stage.setTitle("Выбор студента");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setWidth(438D);
            stage.setHeight(314D);
            stage.setMinWidth(438D);
            stage.setMinHeight(314D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage createStudentRegistrationStage(Stage mainStage, Test test) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_registration.fxml"));
            Parent root = loader.load();

            StudentRegistrationController studentRegistrationController = loader.getController();
            studentRegistrationController.setCurrentStage(stage);
            studentRegistrationController.setMainStage(mainStage);
            studentRegistrationController.setTest(test);

            stage.setTitle("Регистрация студента");
            stage.setScene(new Scene(root));
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setMinWidth(251D);
            stage.setMinHeight(162D);
            stage.setWidth(300D);
            stage.setHeight(190D);
            stage.setResizable(false);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage createStudentListStage(Stage mainStage) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_window.fxml"));
            Parent root = loader.load();

            StudentWindowController studentWindowController = loader.getController();
            studentWindowController.setMainStage(mainStage);
            studentWindowController.setCurrentStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Список студентов");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setWidth(380D);
            stage.setHeight(360D);
            stage.setMinWidth(380D);
            stage.setMinHeight(360D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage.getScene().getWindow());
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage createStudentSaveStage(Stage parentStage, String title, Student student, RefreshableController parentController) {
        try {
            Stage dialogStage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_save_window.fxml"));
            Parent root = loader.load();

            StudentSaveWindowController studentSaveWindowController = loader.getController();
            studentSaveWindowController.setModalStage(dialogStage);
            studentSaveWindowController.setStudent(student);
            studentSaveWindowController.setParentController(parentController);
            studentSaveWindowController.postInitialize();

            dialogStage.setScene(new Scene(root));
            dialogStage.getIcons().add(ReferenceData.getLogoImage());
            dialogStage.setTitle(title);
            dialogStage.setResizable(false);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parentStage);
            return dialogStage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage createStudentStatisticsStage(Stage parentStage, Student student) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_statistics_window.fxml"));
            Parent root = loader.load();

            StudentStatisticsWindowController studentStatisticsWindowController = loader.getController();
            studentStatisticsWindowController.setCurrentStage(stage);
            studentStatisticsWindowController.setStudent(student);
            studentStatisticsWindowController.postInitialize();

            stage.setScene(new Scene(root));
            stage.setTitle("Статистика студента");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setWidth(400D);
            stage.setHeight(400D);
            stage.setMinWidth(400D);
            stage.setMinHeight(400D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage createStudentStatisticsChartStage(Stage parentStage, Student student, Subject subject) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_statistics_chart_window.fxml"));
            Parent root = loader.load();

            StudentStatisticsChartWindowController statisticsChartWindowController = loader.getController();
            statisticsChartWindowController.setCurrentStage(stage);
            statisticsChartWindowController.setStudent(student);
            statisticsChartWindowController.setSubject(subject);
            statisticsChartWindowController.postInitialize();

            stage.setScene(new Scene(root));
            stage.setTitle("Статистика студента");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setWidth(600D);
            stage.setHeight(600D);
            stage.setMinWidth(500D);
            stage.setMinHeight(500D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}