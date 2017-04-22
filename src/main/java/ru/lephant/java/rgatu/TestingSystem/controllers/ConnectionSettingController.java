package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.util.HibernateUtil;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.util.Properties;

public class ConnectionSettingController extends AbstractController {

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
            Stage testSelectionStage = TransitionFacade.getTestTransitionService().createTestSelectionStage();
            changePositionOfStage(currentStage, testSelectionStage);
            currentStage.close();
            testSelectionStage.show();
        } else {
            Alert connectionErrorAlert = DialogFactory.createConnectionErrorAlert();
            connectionErrorAlert.show();
        }
    }


    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}