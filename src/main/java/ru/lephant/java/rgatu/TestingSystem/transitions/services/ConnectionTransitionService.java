package ru.lephant.java.rgatu.TestingSystem.transitions.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.controllers.ConnectionSettingController;
import ru.lephant.java.rgatu.TestingSystem.reference.ReferenceData;

import java.io.IOException;

public class ConnectionTransitionService {

    public Stage createConnectionSettingStage() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Система тестирования \"Degress\"");
            stage.getIcons().add(ReferenceData.getLogoImage());

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/connection_setting.fxml"));
            Parent root = fxmlLoader.load();

            ConnectionSettingController connectionSettingController = fxmlLoader.getController();
            connectionSettingController.setCurrentStage(stage);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}