package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TestEditingController implements Initializable {

    @FXML
    private TextField testNameField;

    @FXML
    private ComboBox<Teacher> teacherComboBox;
    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();

    @FXML
    private ComboBox<Subject> subjectComboBox;
    private ObservableList<Subject> subjects = FXCollections.observableArrayList();

    @FXML
    private CheckBox randomOrderCheckBox;

    @FXML
    private ListView<Question> questionList;
    private ObservableList<Question> questions = FXCollections.observableArrayList();

    @FXML
    private TextArea questionTextField;

    @FXML
    private VBox choiceBox;

    private Test test;
    private Stage currentStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionList.setItems(questions);
        subjectComboBox.setItems(subjects);
        teacherComboBox.setItems(teachers);

        initializeSubjects();
        initializeTeachers();
    }

    public void fillFields() {
        testNameField.setText(test.getName());
        teacherComboBox.getSelectionModel().select(test.getTeacher());
        subjectComboBox.getSelectionModel().select(test.getSubject());
        randomOrderCheckBox.setSelected(test.isRandomOrder());
//        questions.setAll(test.getQuestions()); TODO
    }


    @FXML
    public void onQuestionListClicked(MouseEvent mouseEvent) {

    }

    @FXML
    public void onAddChoiceButtonClicked(ActionEvent event) {

    }

    @FXML
    public void onSaveTestButtonClicked(ActionEvent event) {
        test.setName(testNameField.getText());
        test.setTeacher(teacherComboBox.getSelectionModel().getSelectedItem());
        test.setSubject(subjectComboBox.getSelectionModel().getSelectedItem());
        test.setRandomOrder(randomOrderCheckBox.isSelected());

        saveTest(test);

        currentStage.close();
    }


    private void initializeTeachers() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List list = session.createCriteria(Teacher.class).list();
            teachers.setAll(list);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void initializeSubjects() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List list = session.createCriteria(Subject.class).list();
            subjects.setAll(list);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void saveTest(Test test) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(test);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public void setTest(Test test) {
        this.test = test;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}