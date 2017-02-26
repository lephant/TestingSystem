package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentRegistrationController implements Initializable {

    @FXML
    private TextField fioField;

    @FXML
    private ComboBox<Group> groupComboBox;
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage modalStage;
    private Test test;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        groups.setAll(DaoFacade.getGroupDAOService().getList());
        groupComboBox.setItems(groups);
    }


    @FXML
    public void onRegistrationClick() {
        Student student = createStudent();
        if (validateNewStudent(student)) {
            DaoFacade.getStudentDAOService().save(student);
            doTransitionToTestExecution(student);
        } else {
            showAlertAboutWrongData();
        }
    }

    @FXML
    public void onCancelClick() {
        doTransitionToStudentSelectionScene();
    }


    private Student createStudent() {
        Student student = new Student(fioField.getText());
        Group group = groupComboBox.getSelectionModel().getSelectedItem();
        student.setGroup(group);
        return student;
    }

    private boolean validateNewStudent(Student student) {
        if (student.getGroup() == null) return false;
        if (student.getFio() == null || student.getFio().trim().isEmpty()) return false;
        if (student.getFio().length() > 255) return false;
        return true;
    }

    private void doTransitionToTestExecution(Student student) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/test_execution.fxml"));
            Parent root = fxmlLoader.load();
            TestExecutionController testExecutionController = fxmlLoader.getController();
            testExecutionController.setMainStage(mainStage);
            test = DaoFacade.getTestDAOService().getByPK(test.getId());
            testExecutionController.setTest(test);
            testExecutionController.setStudent(student);
            testExecutionController.initializeQuestionList();
            testExecutionController.showQuestion(0);
            mainStage.setScene(new Scene(root));
            mainStage.setResizable(true);
            mainStage.setWidth(700D);
            mainStage.setHeight(500D);
            mainStage.setMinWidth(700D);
            mainStage.setMinHeight(500D);
            modalStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void doTransitionToStudentSelectionScene() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_selection.fxml"));
            Parent root = loader.load();

            StudentSelectionController studentSelectionController = loader.getController();
            studentSelectionController.setModalStage(modalStage);
            studentSelectionController.setMainStage(mainStage);
            studentSelectionController.setTest(test);

            modalStage.setTitle("Выбор студента");
            modalStage.setResizable(true);
            modalStage.setWidth(438D);
            modalStage.setHeight(314D);
            modalStage.setMinWidth(438D);
            modalStage.setMinHeight(314D);

            modalStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}