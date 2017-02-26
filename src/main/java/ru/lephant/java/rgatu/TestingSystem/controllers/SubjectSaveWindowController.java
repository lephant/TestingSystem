package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;

public class SubjectSaveWindowController {

    @FXML
    private TextField nameField;

    private Stage modalStage;
    private Subject subject;

    private boolean needToSave;


    public void fillFields() {
        nameField.setText(subject.getName());
    }


    @FXML
    public void onSaveButtonClicked() {
        if (validateSubject()) {
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


    private boolean validateSubject() {
        String name = nameField.getText();
        if (name.trim().length() < 1) return false;
        if (name.length() > 128) return false;
        return true;
    }

    private void applyChanges() {
        subject.setName(nameField.getText());
    }


    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public boolean isNeedToSave() {
        return needToSave;
    }

    public void setNeedToSave(boolean needToSave) {
        this.needToSave = needToSave;
    }
}