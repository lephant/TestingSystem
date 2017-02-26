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

import java.net.URL;
import java.util.ResourceBundle;

public class StudentSaveWindowController implements Initializable {

    @FXML
    private TextField fioField;

    @FXML
    private ListView<Group> groupListView;
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    private Stage modalStage;
    private Student student;

    private boolean needToSave;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        groups.setAll(DaoFacade.getGroupDAOService().getList());
        groupListView.setItems(groups);
    }

    public void fillFields() {
        fioField.setText(student.getFio());
        if (student.getGroup() != null) {
            groupListView.getSelectionModel().select(student.getGroup());
        }
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
        if (groupListView.getSelectionModel().getSelectedIndex() == -1) return false;
        return true;
    }

    private void applyChanges() {
        student.setFio(fioField.getText());
        student.setGroup(groupListView.getSelectionModel().getSelectedItem());
    }


    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isNeedToSave() {
        return needToSave;
    }

    public void setNeedToSave(boolean needToSave) {
        this.needToSave = needToSave;
    }
}