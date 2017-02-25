package ru.lephant.java.rgatu.TestingSystem.dialogs;

import javafx.scene.control.Alert;

public class NoSelectedItemAlert {

    public NoSelectedItemAlert(String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка!");
        alert.setHeaderText(headerText);
        alert.setContentText(null);
        alert.showAndWait();
        alert.close();
    }
}