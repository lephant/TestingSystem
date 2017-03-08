package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.entities.User;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;
import ru.lephant.java.rgatu.TestingSystem.interfaces.PostInitializable;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.reference.ReferenceData;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TestSelectionController implements Initializable, RefreshableController, PostInitializable {

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

    private Stage currentStage;


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
    public void postInitialize() {
        currentStage.setOnCloseRequest(event -> HibernateUtil.closeSessionFactory());
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
        currentStage.close();
        HibernateUtil.closeSessionFactory();
    }

    @FXML
    public void onShowStudentsMenuItemClicked() {
        Stage studentListStage = TransitionFacade.getStudentTransitionService().createStudentListStage(currentStage);
        studentListStage.show();
    }

    @FXML
    public void onShowTeachersMenuItemClicked() {
        Stage teacherStage = TransitionFacade.getTeacherTransitionService().createTeacherListStage(currentStage);
        teacherStage.show();
    }

    @FXML
    public void onShowSubjectsMenuItemClicked() {
        Stage subjectStage = TransitionFacade.getSubjectTransitionService().createSubjectListStage(currentStage);
        subjectStage.show();
    }

    @FXML
    public void onShowGroupsMenuItemClicked() {
        Stage groupStage = TransitionFacade.getGroupTransitionService().createGroupListStage(currentStage);
        groupStage.show();
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
        Stage testEditingStage = TransitionFacade.getTestTransitionService().createTestEditingStage(currentStage, new Test(), this);
        testEditingStage.show();
    }

    @FXML
    public void onTestEditMenuItemClicked() {
        Test test = testTableView.getSelectionModel().getSelectedItem();
        if (test != null) {
            Stage testEditingStage = TransitionFacade.getTestTransitionService().createTestEditingStage(currentStage, test, this);
            testEditingStage.show();
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
        dialog.setGraphic(new ImageView(ReferenceData.getLoginImage()));

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
            Stage studentSelectionStage = TransitionFacade.getStudentTransitionService().createStudentSelectionStage(currentStage, test);
            studentSelectionStage.show();
        } else {
            new NoSelectedItemAlert("Не выбран тест для выполнения!");
        }
    }


    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}