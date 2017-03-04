package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.interfaces.PostInitializable;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.validators.impl.GroupValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupSaveWindowController implements Initializable, PostInitializable {

    @FXML
    private TextField nameField;

    private Stage modalStage;
    private Group group;

    private RefreshableController parentController;

    private GroupValidator groupValidator = new GroupValidator();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> group.setName(newValue));
    }

    @Override
    public void postInitialize() {
        modalStage.setOnCloseRequest(event -> parentController.refreshData());
        nameField.setText(group.getName());
    }


    @FXML
    public void onSaveButtonClicked() {
        if (groupValidator.validate(group)) {
            DaoFacade.getGroupDAOService().save(group);
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

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setParentController(RefreshableController parentController) {
        this.parentController = parentController;
    }
}