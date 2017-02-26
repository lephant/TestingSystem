package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;

public class TeacherSaveWindowController {

    @FXML
    private TextField fioField;

    private Stage modalStage;
    private Teacher teacher;

    private boolean needToSave;


    public void fillFields() {
        fioField.setText(teacher.getFio());
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
        String name = fioField.getText();
        if (name.trim().length() < 1) return false;
        if (name.length() > 255) return false;
        return true;
    }

    private void applyChanges() {
        teacher.setFio(fioField.getText());
    }


    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public boolean isNeedToSave() {
        return needToSave;
    }

    public void setNeedToSave(boolean needToSave) {
        this.needToSave = needToSave;
    }
}