package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.interfaces.PostInitializable;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.validators.impl.StudentValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentSaveWindowController implements Initializable, PostInitializable {

    @FXML
    private TextField fioField;

    @FXML
    private ListView<Group> groupListView;
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    private Stage modalStage;
    private Student student;
    private RefreshableController parentController;

    private StudentValidator studentValidator = new StudentValidator();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        groups.setAll(DaoFacade.getGroupDAOService().getList());
        groupListView.setItems(groups);
        fioField.textProperty().addListener((observable, oldValue, newValue) -> student.setFio(newValue));
    }

    @Override
    public void postInitialize() {
        modalStage.setOnCloseRequest(event -> parentController.refreshData());
        fioField.setText(student.getFio());
        if (student.getGroup() != null) {
            groupListView.getSelectionModel().select(student.getGroup());
        }
    }


    @FXML
    public void onSaveButtonClicked() {
        student.setGroup(groupListView.getSelectionModel().getSelectedItem());
        if (studentValidator.validate(student)) {
            DaoFacade.getStudentDAOService().save(student);
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

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setParentController(RefreshableController parentController) {
        this.parentController = parentController;
    }
}