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
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.drawers.QuestionDrawerFactory;
import ru.lephant.java.rgatu.TestingSystem.drawers.questiondrawers.QuestionDrawer;
import ru.lephant.java.rgatu.TestingSystem.entities.*;
import ru.lephant.java.rgatu.TestingSystem.resolvers.ToggleGroupResolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionList.setItems(questions);
        subjectComboBox.setItems(subjects);
        teacherComboBox.setItems(teachers);

        subjects.setAll(DaoFacade.getSubjectDAOService().getList());
        teachers.setAll(DaoFacade.getTeacherDAOService().getList());

        toggleGroupResolver = new ToggleGroupResolver();
        questionDrawerFactory = new QuestionDrawerFactory();

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
        Class clazz = calculateQuestionClass(currentQuestion);
        if (clazz == SingleChoiceQuestion.class) {
            SingleChoiceQuestion question = (SingleChoiceQuestion) currentQuestion;
            ToggleGroup toggleGroup = toggleGroupResolver.resolve(question);
            Choice choice = new Choice(question, "Новый вариант", false);
            question.getChoices().add(choice);

            RadioButton radioButton = new RadioButton();
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setText("Новый вариант");
            radioButton.setSelected(false);

            radioButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    choice.setCorrectIt(radioButton.isSelected());
                }
            });

            addContextMenuToOption(choice, radioButton);
            choiceBox.getChildren().add(radioButton);
        } else if (clazz == MultiChoiceQuestion.class) {
            MultiChoiceQuestion question = (MultiChoiceQuestion) currentQuestion;
            Choice choice = new Choice(question, "Новый вариант", false);
            question.getChoices().add(choice);

            CheckBox checkBox = new CheckBox();
            checkBox.setText("Новый вариант");
            checkBox.setSelected(false);

            checkBox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    choice.setCorrectIt(checkBox.isSelected());
                }
            });

            addContextMenuToOption(choice, checkBox);
            choiceBox.getChildren().add(checkBox);
        } else if (clazz == MissingWordQuestion.class) {
            MissingWordQuestion question = (MissingWordQuestion) currentQuestion;
            MissingPossibleAnswer possibleAnswer = new MissingPossibleAnswer(question, "Новый вариант");
            question.getPossibleAnswers().add(possibleAnswer);

            TextField possibleAnswerField = new TextField();
            possibleAnswerField.setText("Новый вариант");
            possibleAnswerField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    possibleAnswer.setText(newValue);
                }
            });

            addContextMenuToOption(possibleAnswer, possibleAnswerField);
            choiceBox.getChildren().add(possibleAnswerField);
        }
    }

    private void addContextMenuToOption(MissingPossibleAnswer possibleAnswer, TextField possibleAnswerField) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editMenuItem = new MenuItem("Редактировать");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog(possibleAnswer.getText());
                dialog.setTitle("Редактирование");
                dialog.setHeaderText(null);
                dialog.setContentText("Введите текст варианта ответа:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    possibleAnswer.setText(result.get());
                    possibleAnswerField.setText(result.get());
                }
            }
        });

        MenuItem deleteMenuItem = new MenuItem("Удалить");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((MissingWordQuestion) currentQuestion).getPossibleAnswers().remove(possibleAnswer);
                choiceBox.getChildren().remove(possibleAnswerField);
            }
        });

        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);
        possibleAnswerField.setContextMenu(contextMenu);
    }

    //TODO: перегрузить
    private void addContextMenuToOption(final Choice choice, final ButtonBase buttonBase) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editMenuItem = new MenuItem("Редактировать");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog(choice.getText());
                dialog.setTitle("Редактирование");
                dialog.setHeaderText(null);
                dialog.setContentText("Введите текст варианта ответа:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    choice.setText(result.get());
                    buttonBase.setText(result.get());
                }
            }
        });

        MenuItem deleteMenuItem = new MenuItem("Удалить");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((SingleChoiceQuestion) currentQuestion).getChoices().remove(choice);
                choiceBox.getChildren().remove(buttonBase);
            }
        });

        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);
        buttonBase.setContextMenu(contextMenu);
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