package ru.lephant.java.rgatu.TestingSystem.transitions.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.controllers.SubjectSaveWindowController;
import ru.lephant.java.rgatu.TestingSystem.controllers.SubjectWindowController;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import java.io.IOException;

public class SubjectTransitionService {

    public Stage createSubjectListStage(Stage mainStage) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/subject_window.fxml"));
            Parent root = loader.load();

            SubjectWindowController subjectWindowController = loader.getController();
            subjectWindowController.setMainStage(mainStage);
            subjectWindowController.setCurrentStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Список предметов");
            stage.getIcons().add(new Image("/test.png"));

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

    public Stage createSubjectSaveStage(Stage parentStage, String title, Subject subject, RefreshableController parentController) {
        try {
            Stage dialogStage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/subject_save_window.fxml"));
            Parent root = loader.load();

            SubjectSaveWindowController subjectSaveWindowController = loader.getController();
            subjectSaveWindowController.setModalStage(dialogStage);
            subjectSaveWindowController.setSubject(subject);
            subjectSaveWindowController.setParentController(parentController);
            subjectSaveWindowController.postInitialize();

            dialogStage.setScene(new Scene(root));
            dialogStage.getIcons().add(new Image("/test.png"));
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