package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.validators.impl.TeacherValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherSaveWindowController extends AbstractController {

    @FXML
    private TextField fioField;

    private Stage modalStage;
    private Teacher teacher;

    private TeacherValidator teacherValidator = new TeacherValidator();

    private RefreshableController parentController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fioField.textProperty().addListener((observable, oldValue, newValue) -> teacher.setFio(newValue));
    }

    @Override
    public void postInitialize() {
        modalStage.setOnCloseRequest(event -> parentController.refreshData());
        fioField.setText(teacher.getFio());
    }


    @FXML
    public void onSaveButtonClicked() {
        if (teacherValidator.validate(teacher)) {
            DaoFacade.getTeacherDAOService().save(teacher);
            parentController.refreshData();
            modalStage.close();
        }
    }

    @FXML
    public void onCancelButtonClicked() {
        parentController.refreshData();
        modalStage.close();
    }


    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setParentController(RefreshableController parentController) {
        this.parentController = parentController;
    }
}