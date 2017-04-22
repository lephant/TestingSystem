package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.util.HibernateUtil;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TestSelectionController extends AbstractController implements RefreshableController {

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
    private MenuItem showSettingMenuItem;

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
    public void onShowSettingMenuItemClicked() {
        Stage connectionSettingStage = TransitionFacade.getConnectionTransitionService().createConnectionSettingStage();
        changePositionOfStage(currentStage, connectionSettingStage);
        HibernateUtil.closeSessionFactory();
        currentStage.close();
        connectionSettingStage.show();
    }

    @FXML
    public void onLogInMenuItemClicked() {
        Dialog<Pair<String, String>> logInDialog = DialogFactory.createLogInDialog();
        Optional<Pair<String, String>> usernameAndPassword = logInDialog.showAndWait();
        if (usernameAndPassword.isPresent()) {
            String username = usernameAndPassword.get().getKey();
            String password = usernameAndPassword.get().getValue();
            if (DaoFacade.getUserDAOService().checkUserByUsernameAndPassword(username, password)) {
                authorize(true);
            } else {
                DialogFactory.createAuthorizationErrorAlert().show();
            }
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
        changePositionOfStage(currentStage, studentListStage);
        currentStage.hide();
        studentListStage.show();
    }

    @FXML
    public void onShowTeachersMenuItemClicked() {
        Stage teacherStage = TransitionFacade.getTeacherTransitionService().createTeacherListStage(currentStage);
        changePositionOfStage(currentStage, teacherStage);
        currentStage.hide();
        teacherStage.show();
    }

    @FXML
    public void onShowSubjectsMenuItemClicked() {
        Stage subjectStage = TransitionFacade.getSubjectTransitionService().createSubjectListStage(currentStage);
        changePositionOfStage(currentStage, subjectStage);
        currentStage.hide();
        subjectStage.show();
    }

    @FXML
    public void onShowGroupsMenuItemClicked() {
        Stage groupStage = TransitionFacade.getGroupTransitionService().createGroupListStage(currentStage);
        changePositionOfStage(currentStage, groupStage);
        currentStage.hide();
        groupStage.show();
    }

    @FXML
    public void onShowResultsMenuItemClicked() {
        Stage studentResultsListStage = TransitionFacade.getStudentResultsTransitionService().createStudentResultsListStage(currentStage);
        changePositionOfStage(currentStage, studentResultsListStage);
        currentStage.hide();
        studentResultsListStage.show();
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
        changePositionOfStage(currentStage, testEditingStage);
        currentStage.hide();
        testEditingStage.show();
    }

    @FXML
    public void onTestEditMenuItemClicked() {
        Test test = testTableView.getSelectionModel().getSelectedItem();
        if (test != null) {
            Stage testEditingStage = TransitionFacade.getTestTransitionService().createTestEditingStage(currentStage, test, this);
            changePositionOfStage(currentStage, testEditingStage);
            currentStage.hide();
            testEditingStage.show();
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран тест для редактирования!");
            noSelectedItemAlert.show();
        }
    }

    @FXML
    public void onTestDeleteMenuItemClicked() {
        Test test = testTableView.getSelectionModel().getSelectedItem();
        if (test != null) {
            Alert confirmationAlert = DialogFactory.createConfirmationAlert("Вы уверены, что хотите удалить выбранный тест?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (DaoFacade.getTestDAOService().delete(test)) {
                    tests.remove(test);
                } else {
                    Alert deletingErrorAlert = DialogFactory.createDeletingErrorAlert("Не получилось удалить этот тест.");
                    deletingErrorAlert.show();
                }
            }
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран тест для удаления!");
            noSelectedItemAlert.show();
        }
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

        showSettingMenuItem.setVisible(isAuthorized);

        logInMenuItem.setVisible(!isAuthorized);
        logOutMenuItem.setVisible(isAuthorized);

        actionMenu.setVisible(isAuthorized);

        createTestMenuItem.setVisible(isAuthorized);
    }

    private void testSelected() {
        Test test = testTableView.getSelectionModel().getSelectedItem();
        if (test != null) {
            Stage studentSelectionStage = TransitionFacade.getStudentTransitionService().createStudentSelectionStage(currentStage, test);
            changePositionOfStage(currentStage, studentSelectionStage);
            studentSelectionStage.show();
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран тест для выполнения!");
            noSelectedItemAlert.show();
        }
    }


    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}