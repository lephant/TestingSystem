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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

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
    private Stage modalStage;
    private Test test;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllStudents();
        fillListView(allStudents);
        addFioFieldListener();
        studentsList.setItems(students);
    }


    public void testStartButtonClicked() {
        studentSelected();
    }

    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            studentSelected();
        }
    }

    public void onMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            studentSelected();
        }
    }

    public void registrationButtonClicked() {
        doTransitionToStudentRegistrationScene();
    }


    private void doTransitionToStudentRegistrationScene() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_registration.fxml"));
            Parent root = loader.load();

            StudentRegistrationController studentRegistrationController = loader.getController();
            studentRegistrationController.setModalStage(modalStage);
            studentRegistrationController.setMainStage(mainStage);
            studentRegistrationController.setTest(test);

            modalStage.setTitle("Регистрация студента");
            modalStage.setMinWidth(251D);
            modalStage.setMinHeight(162D);
            modalStage.setWidth(300D);
            modalStage.setHeight(190D);
            modalStage.setResizable(false);
            modalStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void studentSelected() {
        Student student = studentsList.getSelectionModel().getSelectedItem();
        if (student != null) {
            System.out.println(student);//TODO: реализовать переход !!!с передачей студента!!!
        }
    }

    @SuppressWarnings("unchecked")
    private void getAllStudents() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            allStudents = session
                    .createCriteria(Student.class)
                    .list();
        } finally {
            if (session != null) {
                session.close();
            }
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

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}