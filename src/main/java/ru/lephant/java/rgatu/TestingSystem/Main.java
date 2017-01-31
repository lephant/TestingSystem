package ru.lephant.java.rgatu.TestingSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.controllers.ConnectionSettingController;
import ru.lephant.java.rgatu.TestingSystem.controllers.TestSelectionController;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setTitle("Система тестирования \"Пирожок\"");

        mainStage.setOnCloseRequest(event -> {
            HibernateUtil.closeSessionFactory();
            mainStage.close();
        });

        if (HibernateUtil.checkConnection()) {
            showTestSelectionScene(mainStage);
        } else {
            showConnectionSettingScene(mainStage);
        }
    }

    private void showConnectionSettingScene(Stage mainStage) throws java.io.IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/connection_setting.fxml"));
        Parent root = fxmlLoader.load();
        ConnectionSettingController connectionSettingController = fxmlLoader.getController();
        connectionSettingController.setMainStage(mainStage);
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    private void showTestSelectionScene(Stage mainStage) throws java.io.IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/test_selection.fxml"));
        Parent root = fxmlLoader.load();
        TestSelectionController testSelectionController = fxmlLoader.getController();
        testSelectionController.setMainStage(mainStage);
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

}
