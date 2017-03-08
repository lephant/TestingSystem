package ru.lephant.java.rgatu.TestingSystem.transitions.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.controllers.TeacherSaveWindowController;
import ru.lephant.java.rgatu.TestingSystem.controllers.TeacherWindowController;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.reference.ReferenceData;

import java.io.IOException;

public class TeacherTransitionService {

    public Stage createTeacherListStage(Stage mainStage) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/teacher_window.fxml"));
            Parent root = loader.load();

            TeacherWindowController teacherWindowController = loader.getController();
            teacherWindowController.setMainStage(mainStage);
            teacherWindowController.setCurrentStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Список преподавателей");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setWidth(380D);
            stage.setHeight(360D);
            stage.setMinWidth(380D);
            stage.setMinHeight(360D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage createTeacherSaveStage(Stage parentStage, String title, Teacher teacher, RefreshableController parentController) {
        try {
            Stage dialogStage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/teacher_save_window.fxml"));
            Parent root = loader.load();

            TeacherSaveWindowController teacherSaveWindowController = loader.getController();
            teacherSaveWindowController.setModalStage(dialogStage);
            teacherSaveWindowController.setTeacher(teacher);
            teacherSaveWindowController.setParentController(parentController);
            teacherSaveWindowController.postInitialize();

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

}