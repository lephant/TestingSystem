package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.QuestionDrawerFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.imagedrawers.ImageDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;
import ru.lephant.java.rgatu.TestingSystem.interfaces.PostInitializable;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.TestChecker;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker.DefaultTestChecker;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class TestExecutionController implements Initializable, PostInitializable {

    @FXML
    private ListView<String> questionList;
    private ObservableList<String> questionListData = FXCollections.observableArrayList();

    @FXML
    private Label testNameLabel;

    @FXML
    private Label numberAndQuestionTextLabel;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private VBox choiceBox;

    private Stage mainStage;
    private Stage currentStage;

    private Test test;
    private Student student;

    private int questionNumber;

    private ImageDrawer imageDrawer;
    private QuestionDrawerFactory questionDrawerFactory;
    private TestChecker testChecker;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageDrawer = new ImageDrawer();
        questionDrawerFactory = new QuestionDrawerFactory();
        testChecker = new DefaultTestChecker();
        questionList.setItems(questionListData);
    }

    @Override
    public void postInitialize() {
        currentStage.setOnCloseRequest(event -> mainStage.show());
        test = DaoFacade.getTestDAOService().getByPK(test.getId());
        if (test.isRandomOrder()) {
            Collections.shuffle(test.getQuestions());
        }
        for (int i = 1; i <= test.getQuestions().size(); i++) {
            questionListData.add("Вопрос " + i);
        }
    }


    @FXML
    public void onTestCompletion() {
        Alert confirmationAlert = DialogFactory.createConfirmationAlert("Вы действительно хотите завершить тест?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TestOfStudent testOfStudent = composeTestOfStudent();
            DaoFacade.getStudentResultsDAOService().save(testOfStudent);
            showResult(testOfStudent);
            currentStage.close();
            mainStage.show();
        }
    }

    private TestOfStudent composeTestOfStudent() {
        TestOfStudent testOfStudent = null;
        try {
            testOfStudent = new TestOfStudent();
            testOfStudent.setTest(test);
            testOfStudent.setStudent(student);
            testOfStudent.setResult(BigDecimal.valueOf(testChecker.check(test)));
            testOfStudent.setComputerName(InetAddress.getLocalHost().getHostName());
            testOfStudent.setDateAndTime(new Date());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return testOfStudent;
    }

    private void showResult(TestOfStudent testOfStudent) {
        Alert alert = DialogFactory.createResultInformationAlert("Ваши результаты по тесту:\n" +
                "Название теста: " + testOfStudent.getTest().getName() + "\n" +
                "Предмет: " + testOfStudent.getTest().getSubject().getName() + "\n" +
                "Преподаватель: " + testOfStudent.getTest().getTeacher().getFio() + "\n" +
                "Вы: " + testOfStudent.getStudent().getFio() + " (" + testOfStudent.getStudent().getGroup().getName() + ")\n" +
                "Результат: " + ((double) ((int) (testOfStudent.getResult().doubleValue() * 10000)) / 100.0) + "%" +
                "\n\n" +
                "Результат успешно сохранен на сервер!"
        );
        alert.showAndWait();
    }


    public void showQuestion(int questionNumber) {
        this.questionNumber = questionNumber;
        questionList.getSelectionModel().select(questionNumber);
        Question question = test.getQuestions().get(questionNumber);
        configureButtonProperties();
        changeTitles(question);
        drawQuestion(question);
    }

    @FXML
    public void onQuestionListClicked() {
        int index = questionList.getSelectionModel().getSelectedIndex();
        if (index != questionNumber) {
            showQuestion(index);
        }
    }

    @FXML
    public void prevQuestionButton() {
        showQuestion(--questionNumber);
    }

    @FXML
    public void nextQuestionButton() {
        showQuestion(++questionNumber);
    }

    private void configureButtonProperties() {
        prevButton.setDisable(questionNumber <= 0);
        nextButton.setDisable(questionNumber >= test.getQuestions().size() - 1);
    }

    private void changeTitles(Question question) {
        testNameLabel.setText(test.getName());
        numberAndQuestionTextLabel.setText("Вопрос " + (questionNumber + 1) + ": " + question.getText());
    }

    private void drawQuestion(Question question) {
        choiceBox.getChildren().clear();
        if (question.getImage() != null) {
            imageDrawer.drawImage(question, choiceBox, 0);
        }
        QuestionDrawer questionDrawer = questionDrawerFactory.getQuestionDrawer(question);
        questionDrawer.draw(question, choiceBox, false);
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

    public void setStudent(Student student) {
        this.student = student;
    }
}