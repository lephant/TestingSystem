package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.entities.*;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class TestExecutionController implements Initializable {

    private static final double IMAGE_VIEW_DEFAULT_WIDTH = 450D;
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
    private Test test;
    private Student student;

    private int questionNumber;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionList.setItems(questionListData);
    }

    public void initializeQuestionList() {
        for (int i = 1; i <= test.getQuestions().size(); i++) {
            questionListData.add("Вопрос " + i);
        }
    }


    @FXML
    public void onTestCompletion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Завершение теста");
        alert.setHeaderText(null);
        alert.setContentText("Вы действительно хотите завершить тест?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            TestOfStudent testOfStudent = composeTestOfStudent();
            saveTestResult(testOfStudent);
            showResult(testOfStudent);
            doTransitionToTestSelectionScene();
        } else {
            alert.close();
        }
    }

    private TestOfStudent composeTestOfStudent() {
        TestOfStudent testOfStudent = null;
        try {
            testOfStudent = new TestOfStudent();
            testOfStudent.setTest(test);
            testOfStudent.setStudent(student);
            testOfStudent.setResult(BigDecimal.valueOf(test.calculateResult()));
            testOfStudent.setComputerName(InetAddress.getLocalHost().getHostName());
            testOfStudent.setDateAndTime(new Date());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return testOfStudent;
    }

    private void saveTestResult(TestOfStudent testOfStudent) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(testOfStudent);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void showResult(TestOfStudent testOfStudent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Результаты");
        alert.setHeaderText(null);
        alert.setContentText("Ваши результаты по тесту:\n" +
                "Название теста: " + testOfStudent.getTest().getName() + "\n" +
                "Предмет: " + testOfStudent.getTest().getSubject() + "\n" +
                "Преподаватель: " + testOfStudent.getTest().getTeacherFio() + "\n" +
                "Вы: " + testOfStudent.getStudent().getFio() + " (" + testOfStudent.getStudent().getGroup().getName() + ")\n" +
                "Результат: " + ((double) ((int) (testOfStudent.getResult().doubleValue() * 10000)) / 100.0) + "%" +
                "\n \n" +
                "Результат успешно сохранен на сервер!"
        );
        alert.show();
    }

    private void doTransitionToTestSelectionScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/test_selection.fxml"));
            Parent root = fxmlLoader.load();
            TestSelectionController testSelectionController = fxmlLoader.getController();
            testSelectionController.setMainStage(mainStage);
            mainStage.setScene(new Scene(root));
            mainStage.setResizable(true);
            mainStage.setHeight(400D);
            mainStage.setWidth(420D);
            mainStage.setMinWidth(420D);
            mainStage.setMinHeight(400D);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            drawImage(question);
        }
        Class questionClass = calculateQuestionClass(question);
        if (questionClass == SingleChoiceQuestion.class) {
            drawSingleChoiceQuestion((SingleChoiceQuestion) question);
        } else if (questionClass == MultiChoiceQuestion.class) {
            drawMultiChoiceQuestion((MultiChoiceQuestion) question);
        } else if (questionClass == MissingWordQuestion.class) {
            drawMissingWordQuestion((MissingWordQuestion) question);
        }
    }

    private void drawSingleChoiceQuestion(SingleChoiceQuestion question) {
        ToggleGroup toggleGroup = new ToggleGroup();
        for (Choice choice : question.getChoices()) {
            RadioButton radioButton = new RadioButton(choice.getText());
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for (Choice choice : question.getChoices()) {
                        choice.setMarked(false);
                    }
                    choice.setMarked(true);
                }
            });
            radioButton.setWrapText(true);
            radioButton.setSelected(choice.isMarked());
            choiceBox.getChildren().add(radioButton);
        }
    }

    private void drawMultiChoiceQuestion(MultiChoiceQuestion question) {
        for (Choice choice : question.getChoices()) {
            CheckBox checkBox = new CheckBox(choice.getText());
            checkBox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    choice.setMarked(checkBox.isSelected());
                }
            });
            checkBox.setWrapText(true);
            checkBox.setSelected(choice.isMarked());
            choiceBox.getChildren().add(checkBox);
        }
    }

    private void drawMissingWordQuestion(MissingWordQuestion question) {
        TextField answerField = new TextField();
        answerField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                question.setAnswer(newValue);
            }
        });
        answerField.setText(question.getAnswer());
        choiceBox.getChildren().add(answerField);
    }

    private Class calculateQuestionClass(Question question) {
        if (question instanceof SingleChoiceQuestion) return SingleChoiceQuestion.class;
        if (question instanceof MultiChoiceQuestion) return MultiChoiceQuestion.class;
        if (question instanceof MissingWordQuestion) return MissingWordQuestion.class;
        return null;
    }


    private void drawImage(Question question) {
        InputStream stream = new ByteArrayInputStream(question.getImage().getContent());
        Image image = new Image(stream);
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double imageViewHeight = (IMAGE_VIEW_DEFAULT_WIDTH * imageHeight) / imageWidth;

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_VIEW_DEFAULT_WIDTH);
        imageView.setFitHeight(imageViewHeight);

        choiceBox.getChildren().add(imageView);
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}