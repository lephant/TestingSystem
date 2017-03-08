package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;
import ru.lephant.java.rgatu.TestingSystem.validators.impl.StudentValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentRegistrationController implements Initializable {

    @FXML
    private TextField fioField;

    @FXML
    private ComboBox<Group> groupComboBox;
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage currentStage;
    private Test test;

    private StudentValidator studentValidator;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentValidator = new StudentValidator();
        groups.setAll(DaoFacade.getGroupDAOService().getList());
        groupComboBox.setItems(groups);
    }


    @FXML
    public void onRegistrationClick() {
        Student student = createStudent();
        if (studentValidator.validate(student)) {
            DaoFacade.getStudentDAOService().save(student);
            Stage testExecutionStage = TransitionFacade.getTestTransitionService().createTestExecutionStage(mainStage, test, student);
            mainStage.hide();
            testExecutionStage.show();
        } else {
            showAlertAboutWrongData();
        }
    }

    @FXML
    public void onCancelClick() {
        Stage studentSelectionStage = TransitionFacade.getStudentTransitionService().createStudentSelectionStage(currentStage, test);
        currentStage.close();
        studentSelectionStage.show();
    }


    private Student createStudent() {
        Student student = new Student(fioField.getText());
        Group group = groupComboBox.getSelectionModel().getSelectedItem();
        student.setGroup(group);
        return student;
    }

    private void showAlertAboutWrongData() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Невалидные данные!");
        alert.setContentText("ФИО студента не может быть пустым и не должно превышать 255 символов!\n" +
                "Группа обязательно должна быть задана!\n" +
                "Проверьте введенные данные и попробуйте снова.");
        alert.show();
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}