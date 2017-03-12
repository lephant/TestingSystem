package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

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
        Stage studentRegistrationStage = TransitionFacade.getStudentTransitionService().createStudentRegistrationStage(mainStage, test);
        currentStage.close();
        studentRegistrationStage.show();
    }


    private void studentSelected() {
        Student student = studentsList.getSelectionModel().getSelectedItem();
        if (student != null) {
            Stage testExecutionStage = TransitionFacade.getTestTransitionService().createTestExecutionStage(mainStage, test, student);
            currentStage.close();
            mainStage.hide();
            testExecutionStage.show();
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Студент не выбран!");
            noSelectedItemAlert.show();
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