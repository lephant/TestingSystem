package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.interfaces.PostInitializable;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.validators.impl.SubjectValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class SubjectSaveWindowController implements Initializable, PostInitializable {

    @FXML
    private TextField nameField;

    private Stage modalStage;
    private Subject subject;

    private SubjectValidator subjectValidator;

    private RefreshableController parentController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectValidator = new SubjectValidator();
        nameField.textProperty().addListener((observable, oldValue, newValue) -> subject.setName(newValue));
    }

    @Override
    public void postInitialize() {
        nameField.setText(subject.getName());
        modalStage.setOnCloseRequest(event -> parentController.refreshData());
    }


    @FXML
    public void onSaveButtonClicked() {
        if (subjectValidator.validate(subject)) {
            DaoFacade.getSubjectDAOService().save(subject);
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

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setParentController(RefreshableController parentController) {
        this.parentController = parentController;
    }
}