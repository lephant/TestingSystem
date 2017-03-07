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
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.validators.impl.StudentValidator;

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

    private void doTransitionToTestExecution(Student student) {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/test_execution.fxml"));
            Parent root = fxmlLoader.load();

            TestExecutionController testExecutionController = fxmlLoader.getController();
            testExecutionController.setMainStage(mainStage);
            testExecutionController.setCurrentStage(stage);
            test = DaoFacade.getTestDAOService().getByPK(test.getId());
            testExecutionController.setTest(test);
            testExecutionController.setStudent(student);
            testExecutionController.postInitialize();
            testExecutionController.showQuestion(0);

            stage.setScene(new Scene(root));
            stage.setTitle("Система тестирования \"Degress\"");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(700D);
            stage.setHeight(500D);
            stage.setMinWidth(700D);
            stage.setMinHeight(500D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            stage.show();
            currentStage.close();
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
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_selection.fxml"));
            Parent root = loader.load();

            StudentSelectionController studentSelectionController = loader.getController();
            studentSelectionController.setCurrentStage(stage);
            studentSelectionController.setMainStage(mainStage);
            studentSelectionController.setTest(test);

            stage.setTitle("Выбор студента");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(438D);
            stage.setHeight(314D);
            stage.setMinWidth(438D);
            stage.setMinHeight(314D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            stage.show();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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