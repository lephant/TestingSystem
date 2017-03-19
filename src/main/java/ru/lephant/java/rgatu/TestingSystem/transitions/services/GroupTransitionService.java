package ru.lephant.java.rgatu.TestingSystem.transitions.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.controllers.GroupSaveWindowController;
import ru.lephant.java.rgatu.TestingSystem.controllers.GroupWindowController;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.reference.ReferenceData;

import java.io.IOException;

public class GroupTransitionService {

    public Stage createGroupListStage(Stage mainStage) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/group_window.fxml"));
            Parent root = loader.load();

            GroupWindowController groupWindowController = loader.getController();
            groupWindowController.setMainStage(mainStage);
            groupWindowController.setCurrentStage(stage);
            groupWindowController.postInitialize();

            stage.setScene(new Scene(root));
            stage.setTitle("Список групп");
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

    public Stage createGroupSaveStage(Stage parentStage, String title, Group group, RefreshableController parentController) {
        try {
            Stage dialogStage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/group_save_window.fxml"));
            Parent root = loader.load();

            GroupSaveWindowController groupSaveWindowController = loader.getController();
            groupSaveWindowController.setModalStage(dialogStage);
            groupSaveWindowController.setGroup(group);
            groupSaveWindowController.setParentController(parentController);
            groupSaveWindowController.postInitialize();

            dialogStage.setScene(new Scene(root));
            dialogStage.getIcons().add(ReferenceData.getLogoImage());
            dialogStage.setTitle(title);

            dialogStage.setWidth(350D);
            dialogStage.setHeight(140D);
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