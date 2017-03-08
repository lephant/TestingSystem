package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.drawers.OptionDrawerFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.QuestionDrawerFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.optiondrawers.OptionDrawer;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.interfaces.PostInitializable;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;
import ru.lephant.java.rgatu.TestingSystem.validators.impl.TestValidator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class TestEditingController implements Initializable, PostInitializable {

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
    private Stage mainStage;

    private int questionNumber;
    private Question currentQuestion;

    private QuestionDrawerFactory questionDrawerFactory;
    private OptionDrawerFactory optionDrawerFactory;
    private TestValidator testValidator;

    private RefreshableController parentController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionList.setItems(questions);
        subjectComboBox.setItems(subjects);
        teacherComboBox.setItems(teachers);

        subjects.setAll(DaoFacade.getSubjectDAOService().getList());
        teachers.setAll(DaoFacade.getTeacherDAOService().getList());

        questionDrawerFactory = new QuestionDrawerFactory();
        optionDrawerFactory = new OptionDrawerFactory();
        testValidator = new TestValidator();

        questions.add("Добавить");
    }

    @Override
    public void postInitialize() {
        currentStage.setOnCloseRequest(event -> parentController.refreshData());

        if (test.getId() > 0) {
            test = DaoFacade.getTestDAOService().getByPK(test.getId());
        }
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
            setLock(true);
            return;
        }
        setLock(false);
        this.questionNumber = questionNumber;
        questionList.getSelectionModel().select(questionNumber);
        currentQuestion = test.getQuestions().get(questionNumber);
        changeFields();
        drawQuestion();

        questionTextField.textProperty().addListener((observable, oldValue, newValue) -> currentQuestion.setText(newValue));
    }

    private void clearFields() {
        questionTextField.setText("");
        choiceBox.getChildren().clear();
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
        if (index == questions.size() - 1) {
            Pair<Stage, QuestionCreationController> stageAndController = TransitionFacade.getTestTransitionService().createQuestionAddingStage(currentStage);
            Stage questionAddingStage = stageAndController.getKey();
            QuestionCreationController questionCreationController = stageAndController.getValue();
            questionAddingStage.showAndWait();

            Question createdQuestion = questionCreationController.getCreatedQuestion();
            if (createdQuestion != null) {
                createdQuestion.setTest(test);
                test.getQuestions().add(createdQuestion);
                questions.add(questions.size() - 1, "Вопрос " + questions.size());
                showQuestion(questions.size() - 2);
            } else {
                showQuestion(questionNumber);
            }
        }
        if (index != questionNumber) {
            questionNumber = index;
            showQuestion(questionNumber);
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

        if (testValidator.validate(test)) {
            DaoFacade.getTestDAOService().save(test);
            parentController.refreshData();
            currentStage.close();
        }
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

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setParentController(RefreshableController parentController) {
        this.parentController = parentController;
    }
}