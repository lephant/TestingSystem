package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.io.IOException;
import java.util.Properties;

public class ConnectionSettingController {

    @FXML
    TextField urlField;

    @FXML
    TextField userField;

    @FXML
    TextField passwordField;

    @FXML
    Button connectButton;

    private Stage currentStage;


    @FXML
    public void doConnect() {
        String url = urlField.getText();
        String username = userField.getText();
        String password = passwordField.getText();

        Properties properties = new Properties();
        properties.put("hibernate.connection.url", url);
        properties.put("hibernate.connection.username", username);
        properties.put("hibernate.connection.password", password);

        if (HibernateUtil.checkConnection(properties)) {
            doTransitionToTestSelectionStage();
        } else {
            showConnectionErrorAlert();
        }
    }

    private void doTransitionToTestSelectionStage() {
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
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setMinWidth(420D);
            stage.setMinHeight(400D);

            stage.show();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showConnectionErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось соединиться с базой данных!");
        alert.setContentText("Проверьте введенные данные и попробуйте снова.");
        alert.show();
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}