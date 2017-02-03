package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupSaveWindowController implements Initializable {

    @FXML
    private TextField nameField;

    private Stage modalStage;
    private Group group;

    private boolean needToSave;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void fillFields() {
        nameField.setText(group.getName());
    }


    @FXML
    public void onSaveButtonClicked() {
        if (validateGroup()) {
            applyChanges();
            needToSave = true;
            modalStage.close();
        }
    }

    @FXML
    public void onCancelButtonClicked() {
        needToSave = false;
        modalStage.close();
    }


    private boolean validateGroup() {
        String name = nameField.getText();
        if (name.trim().length() < 1) return false;
        if (name.length() > 45) return false;
        return true;
    }

    private void applyChanges() {
        group.setName(nameField.getText());
    }


    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isNeedToSave() {
        return needToSave;
    }

    public void setNeedToSave(boolean needToSave) {
        this.needToSave = needToSave;
    }
}