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
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.entities.*;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TestEditingController implements Initializable {

    private static final double IMAGE_VIEW_DEFAULT_WIDTH = 450D;


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
    private ListView<String> questionList;
    private ObservableList<String> questions = FXCollections.observableArrayList();

    @FXML
    private TextArea questionTextField;

    @FXML
    private VBox choiceBox;

    private Test test;
    private Stage currentStage;

    private int questionNumber;
    private Question currentQuestion;


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

        for (int i = 1; i <= test.getQuestions().size(); i++) {
            questions.add("Вопрос " + i);
        }

        questions.add("Добавить");
    }


    public void showQuestion(int questionNumber) {
        this.questionNumber = questionNumber;
        questionList.getSelectionModel().select(questionNumber);
        currentQuestion = test.getQuestions().get(questionNumber);
        changeFields();
        drawQuestion();

        //TODO: проверить на работоспособность, чтобы менялась цель (вопрос) (вариант - обращаться к новому методу, выставляющему значения)
        questionTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentQuestion.setText(newValue);
            }
        });
    }

    private void changeFields() {
        questionTextField.setText(currentQuestion.getText());
    }

    private void drawQuestion() {
        choiceBox.getChildren().clear();
        if (currentQuestion.getImage() != null) {
            drawImage(currentQuestion);
        }
        Class questionClass = calculateQuestionClass(currentQuestion);
        if (questionClass == SingleChoiceQuestion.class) {
            drawSingleChoiceQuestion((SingleChoiceQuestion) currentQuestion);
        } else if (questionClass == MultiChoiceQuestion.class) {
            drawMultiChoiceQuestion((MultiChoiceQuestion) currentQuestion);
        } else if (questionClass == MissingWordQuestion.class) {
            drawMissingWordQuestion((MissingWordQuestion) currentQuestion);
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
                        choice.setCorrectIt(false);
                    }
                    choice.setCorrectIt(true);
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
                    choice.setCorrectIt(checkBox.isSelected());
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
        javafx.scene.image.Image image = new javafx.scene.image.Image(stream);
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double imageViewHeight = (IMAGE_VIEW_DEFAULT_WIDTH * imageHeight) / imageWidth;

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_VIEW_DEFAULT_WIDTH);
        imageView.setFitHeight(imageViewHeight);

        choiceBox.getChildren().add(imageView);
    }


    @FXML
    public void onQuestionListClicked() {
        int index = questionList.getSelectionModel().getSelectedIndex();
        if (index != questionNumber) {
            if (index == questions.size() - 1) {
                showQuestionAddingDialog();
            } else {
                questionNumber = index;
                showQuestion(questionNumber);
            }
        }
    }

    private void showQuestionAddingDialog() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/question_creation_window.fxml"));
            Parent root = loader.load();

            QuestionCreationController questionCreationController = loader.getController();
            questionCreationController.setCurrentStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Создание вопроса");
            stage.getIcons().add(new javafx.scene.image.Image("/test.png"));

            stage.setHeight(180D);
            stage.setWidth(300D);
            stage.setResizable(false);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(currentStage.getScene().getWindow());
            stage.showAndWait();

            Question createdQuestion = questionCreationController.getCreatedQuestion();

            if (createdQuestion != null) {
                createdQuestion.setTest(test);
                test.getQuestions().add(createdQuestion);
                questions.add(questions.size() - 1, "Вопрос " + questions.size());
                showQuestion(questions.size() - 2);
            } else {
                showQuestion(questionNumber);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddChoiceButtonClicked() {

    }

    @FXML
    public void onSaveTestButtonClicked() {
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