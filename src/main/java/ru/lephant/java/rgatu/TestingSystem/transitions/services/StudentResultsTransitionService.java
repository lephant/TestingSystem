package ru.lephant.java.rgatu.TestingSystem.transitions.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.controllers.ResultWindowController;
import ru.lephant.java.rgatu.TestingSystem.reference.ReferenceData;

import java.io.IOException;

public class StudentResultsTransitionService {

    public Stage createStudentResultsListStage(Stage mainStage) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/result_window.fxml"));
            Parent root = loader.load();

            ResultWindowController resultWindowController = loader.getController();
            resultWindowController.setMainStage(mainStage);
            resultWindowController.setCurrentStage(stage);
            resultWindowController.postInitialize();

            stage.setScene(new Scene(root));
            stage.setTitle("Список групп");
            stage.getIcons().add(ReferenceData.getLogoImage());

            stage.setResizable(true);
            stage.setWidth(412D);
            stage.setHeight(260D);
            stage.setMinWidth(412D);
            stage.setMinHeight(260D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
