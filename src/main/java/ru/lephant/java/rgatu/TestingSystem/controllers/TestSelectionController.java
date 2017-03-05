package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.entities.User;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TestSelectionController implements Initializable, RefreshableController {


    @FXML
    private TableView<Test> testTableView;
    private ObservableList<Test> tests = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Test, String> nameTableColumn;

    @FXML
    private TableColumn<Test, String> teacherTableColumn;

    @FXML
    private TableColumn<Test, String> subjectTableColumn;

    @FXML
    private ContextMenu testTableContextMenu;

    @FXML
    private MenuItem testEditMenuItem;

    @FXML
    private MenuItem testDeleteMenuItem;

    @FXML
    private MenuItem logInMenuItem;

    @FXML
    private MenuItem logOutMenuItem;

    @FXML
    private Menu actionMenu;

    @FXML
    private Menu createTestMenuItem;

    private Stage mainStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authorize(false);

        tests.setAll(DaoFacade.getTestDAOService().getList());

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacherTableColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        subjectTableColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

        testTableView.setItems(tests);
    }

    @Override
    public void refreshData() {
        tests.setAll(DaoFacade.getTestDAOService().getList());
    }


    @FXML
    public void onLogInMenuItemClicked() {
        Optional<Pair<String, String>> usernameAndPassword = showLogInDialog();
        if (usernameAndPassword.isPresent() && checkLoginAndPasswordInDB(usernameAndPassword)) {
            authorize(true);
        }
    }

    @FXML
    public void onLogOffMenuItemClicked() {
        authorize(false);
    }

    @FXML
    public void onExitMenuItemClicked() {
        mainStage.close();
        HibernateUtil.closeSessionFactory();
    }

    @FXML
    public void onShowStudentsMenuItemClicked() {
        showStudentStage();
    }

    @FXML
    public void onShowTeachersMenuItemClicked() {
        showTeacherStage();
    }

    @FXML
    public void onShowSubjectsMenuItemClicked() {
        showSubjectStage();
    }

    @FXML
    public void onShowGroupsMenuItemClicked() {
        showGroupStage();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            testSelected();
        }
    }

    @FXML
    public void onMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            testSelected();
        }
    }

    @FXML
    public void selectTest() {
        testSelected();
    }

    @FXML
    public void onCreateTestMenuItemClicked() {
        showTestEditingStage(new Test(), true);
    }

    @FXML
    public void onTestEditMenuItemClicked() {
        Test test = testTableView.getSelectionModel().getSelectedItem();
        if (test != null) {
            showTestEditingStage(test, false);
        }
    }

    @FXML
    public void onTestDeleteMenuItemClicked() {
        Test test = testTableView.getSelectionModel().getSelectedItem();
        if (test != null) {
            DaoFacade.getTestDAOService().delete(test);
            tests.remove(test);
        }
    }

    private Optional<Pair<String, String>> showLogInDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Авторизация");
        dialog.setHeaderText("Введите ваши данные:");
        dialog.setGraphic(new ImageView(this.getClass().getResource("/login.png").toString()));

        ButtonType loginButtonType = new ButtonType("Войти", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Пароль");

        grid.add(new Label("Логин:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        return result;
    }

    private boolean checkLoginAndPasswordInDB(Optional<Pair<String, String>> usernameAndPassword) {
        String username = usernameAndPassword.get().getKey();
        String password = usernameAndPassword.get().getValue();

        Session session = null;
        Object noteInDB;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            noteInDB = session.createCriteria(User.class)
                    .add(Restrictions.eq("username", username))
                    .add(Restrictions.eq("password", password))
                    .add(Restrictions.eq("enabled", true))
                    .setMaxResults(1)
                    .uniqueResult();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return noteInDB != null;
    }


    private void authorize(boolean isAuthorized) {
        if (isAuthorized) {
            testTableContextMenu.setOpacity(1D);
        } else {
            testTableContextMenu.setOpacity(0D);
        }

        testEditMenuItem.setDisable(!isAuthorized);
        testEditMenuItem.setVisible(isAuthorized);

        testDeleteMenuItem.setDisable(!isAuthorized);
        testDeleteMenuItem.setVisible(isAuthorized);

        logInMenuItem.setVisible(!isAuthorized);
        logOutMenuItem.setVisible(isAuthorized);

        actionMenu.setVisible(isAuthorized);

        createTestMenuItem.setVisible(isAuthorized);
    }

    private void testSelected() {
        Test test = testTableView.getSelectionModel().getSelectedItem();
        if (test != null) {
            openStudentSelectionModalStage(test);
        } else {
            new NoSelectedItemAlert("Не выбран тест для выполнения!");
        }
    }

    private void openStudentSelectionModalStage(Test test) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_selection.fxml"));
            Parent root = loader.load();

            StudentSelectionController studentSelectionController = loader.getController();
            studentSelectionController.setMainStage(mainStage);
            studentSelectionController.setModalStage(stage);
            studentSelectionController.setTest(test);

            stage.setScene(new Scene(root));
            stage.setTitle("Выбор студента");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(438D);
            stage.setHeight(314D);
            stage.setMinWidth(438D);
            stage.setMinHeight(314D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showGroupStage() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/group_window.fxml"));
            Parent root = loader.load();

            GroupWindowController groupWindowController = loader.getController();
            groupWindowController.setMainStage(mainStage);
            groupWindowController.setModalStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Список групп");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(380D);
            stage.setHeight(360D);
            stage.setMinWidth(380D);
            stage.setMinHeight(360D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSubjectStage() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/subject_window.fxml"));
            Parent root = loader.load();

            SubjectWindowController subjectWindowController = loader.getController();
            subjectWindowController.setMainStage(mainStage);
            subjectWindowController.setModalStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Список предметов");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(380D);
            stage.setHeight(360D);
            stage.setMinWidth(380D);
            stage.setMinHeight(360D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTeacherStage() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/teacher_window.fxml"));
            Parent root = loader.load();

            TeacherWindowController teacherWindowController = loader.getController();
            teacherWindowController.setMainStage(mainStage);
            teacherWindowController.setModalStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Список преподавателей");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(380D);
            stage.setHeight(360D);
            stage.setMinWidth(380D);
            stage.setMinHeight(360D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showStudentStage() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_window.fxml"));
            Parent root = loader.load();

            StudentWindowController studentWindowController = loader.getController();
            studentWindowController.setMainStage(mainStage);
            studentWindowController.setModalStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Список студентов");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(380D);
            stage.setHeight(360D);
            stage.setMinWidth(380D);
            stage.setMinHeight(360D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTestEditingStage(Test test, boolean isNew) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/test_editing.fxml"));
            Parent root = loader.load();

            TestEditingController testEditingController = loader.getController();
            testEditingController.setCurrentStage(stage);
            testEditingController.setParentController(this);

            if (!isNew) {
                test = DaoFacade.getTestDAOService().getByPK(test.getId());
                testEditingController.setTest(test);
                testEditingController.postInitialize();
            } else {
                testEditingController.setTest(test);
            }

            testEditingController.showQuestion(0);

            stage.setScene(new Scene(root));
            stage.setTitle("Конструктор тестов");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(720D);
            stage.setHeight(620D);
            stage.setMinWidth(720D);
            stage.setMinHeight(620D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}