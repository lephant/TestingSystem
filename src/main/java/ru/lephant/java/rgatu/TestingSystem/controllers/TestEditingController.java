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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.drawers.OptionDrawerFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.QuestionDrawerFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.OptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.*;
import ru.lephant.java.rgatu.TestingSystem.resolvers.ToggleGroupResolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class TestEditingController implements Initializable {

    private static final double IMAGE_VIEW_DEFAULT_WIDTH = 450D;


    @FXML
    private TextField testNameField;

    @FXML
    private Button addChoiceButton;

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

    private ToggleGroupResolver toggleGroupResolver;
    private QuestionDrawerFactory questionDrawerFactory;
    private OptionDrawerFactory optionDrawerFactory;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionList.setItems(questions);
        subjectComboBox.setItems(subjects);
        teacherComboBox.setItems(teachers);

        subjects.setAll(DaoFacade.getSubjectDAOService().getList());
        teachers.setAll(DaoFacade.getTeacherDAOService().getList());

        toggleGroupResolver = new ToggleGroupResolver();
        questionDrawerFactory = new QuestionDrawerFactory();
        optionDrawerFactory = new OptionDrawerFactory();

        questions.add("Добавить");
    }

    public void fillFields() {
        testNameField.setText(test.getName());
        teacherComboBox.getSelectionModel().select(test.getTeacher());
        subjectComboBox.getSelectionModel().select(test.getSubject());
        randomOrderCheckBox.setSelected(test.isRandomOrder());

        for (int i = 0; i < test.getQuestions().size(); i++) {
            questions.add(i, "Вопрос " + (i + 1));
        }
    }


    public void showQuestion(int questionNumber) {
        if (questionNumber > test.getQuestions().size() - 1 || questionNumber < 0) {
            clearFields();
            return;
        }
        setLock(false);
        this.questionNumber = questionNumber;
        questionList.getSelectionModel().select(questionNumber);
        currentQuestion = test.getQuestions().get(questionNumber);
        changeFields();
        drawQuestion();

        questionTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentQuestion.setText(newValue);
            }
        });
    }

    private void clearFields() {
        questionTextField.setText("");
        choiceBox.getChildren().clear();
        setLock(true);
    }

    private void setLock(boolean isLock) {
        questionTextField.setDisable(isLock);
        addChoiceButton.setDisable(isLock);
    }

    private void changeFields() {
        questionTextField.setText(currentQuestion.getText());
    }

    private void drawQuestion() {
        choiceBox.getChildren().clear();
        if (currentQuestion.getImage() != null) {
            drawImage(currentQuestion);
        }
        QuestionDrawer questionDrawer = questionDrawerFactory.getQuestionDrawer(currentQuestion);
        questionDrawer.draw(currentQuestion, choiceBox, true);
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
        if (index != questionNumber || questions.size() == 1) {
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
        OptionDrawer optionDrawer = optionDrawerFactory.getOptionDrawer(currentQuestion);
        optionDrawer.draw(currentQuestion, choiceBox);
    }

    @FXML
    public void onSaveTestButtonClicked() {
        test.setName(testNameField.getText());
        test.setTeacher(teacherComboBox.getSelectionModel().getSelectedItem());
        test.setSubject(subjectComboBox.getSelectionModel().getSelectedItem());
        test.setRandomOrder(randomOrderCheckBox.isSelected());

        DaoFacade.getTestDAOService().save(test);

        currentStage.close();
    }

    @FXML
    public void onDeleteMenuItemClicked() {
        int index = questionList.getSelectionModel().getSelectedIndex();
        if (index >= 0 || index != questions.size() - 1) {
            questions.remove(questions.size() - 2);
            String question = questions.get(questionNumber);
            questionList.getSelectionModel().select(question);
            test.getQuestions().remove(index);
            showQuestion(questionNumber);
        }
    }


    public void setTest(Test test) {
        this.test = test;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}