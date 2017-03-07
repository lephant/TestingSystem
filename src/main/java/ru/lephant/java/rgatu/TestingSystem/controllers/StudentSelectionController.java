package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentSelectionController implements Initializable {

    @FXML
    private TextField fioField;

    @FXML
    private ListView<Student> studentsList;
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private List<Student> allStudents;

    private Stage mainStage;
    private Stage currentStage;
    private Test test;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allStudents = new ArrayList<>(DaoFacade.getStudentDAOService().getList());
        fillListView(allStudents);
        addFioFieldListener();
        studentsList.setItems(students);
    }


    @FXML
    public void testStartButtonClicked() {
        studentSelected();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            studentSelected();
        }
    }

    @FXML
    public void onMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            studentSelected();
        }
    }

    @FXML
    public void registrationButtonClicked() {
        doTransitionToStudentRegistrationScene();
    }


    private void doTransitionToStudentRegistrationScene() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_registration.fxml"));
            Parent root = loader.load();

            StudentRegistrationController studentRegistrationController = loader.getController();
            studentRegistrationController.setCurrentStage(stage);
            studentRegistrationController.setMainStage(mainStage);
            studentRegistrationController.setTest(test);

            stage.setTitle("Регистрация студента");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/test.png"));

            stage.setMinWidth(251D);
            stage.setMinHeight(162D);
            stage.setWidth(300D);
            stage.setHeight(190D);
            stage.setResizable(false);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            stage.show();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void studentSelected() {
        Student student = studentsList.getSelectionModel().getSelectedItem();
        if (student != null) {
            doTransitionToTestExecution(student);
        } else {
            new NoSelectedItemAlert("Студент не выбран!");
        }
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

    private void addFioFieldListener() {
        fioField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                List<Student> res = search(newValue);
                fillListView(res);
            }
        });
    }

    private void fillListView(List<Student> res) {
        students.setAll(res);
    }

    private List<Student> search(String text) {
        List<Student> res = new ArrayList<>();
        for (Student student : allStudents) {
            if (student.getFio().toLowerCase().contains(text.toLowerCase())) {
                res.add(student);
            }
        }
        return res;
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