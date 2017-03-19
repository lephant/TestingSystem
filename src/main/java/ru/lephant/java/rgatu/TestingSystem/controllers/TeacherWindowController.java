package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TeacherWindowController extends AbstractController implements RefreshableController {

    @FXML
    private ListView<Teacher> teacherListView;
    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage currentStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherListView.setItems(teachers);
        teachers.setAll(DaoFacade.getTeacherDAOService().getList());
    }

    @Override
    public void postInitialize() {
        currentStage.setOnCloseRequest(event -> {
            changePositionOfStage(currentStage, mainStage);
            mainStage.show();
        });
    }

    @Override
    public void refreshData() {
        teachers.setAll(DaoFacade.getTeacherDAOService().getList());
    }


    @FXML
    public void onAddButtonClicked() {
        Teacher teacher = new Teacher();
        Stage teacherSaveStage = TransitionFacade.getTeacherTransitionService().createTeacherSaveStage(currentStage, "Добавление преподавателя", teacher, this);
        changePositionOfStage(currentStage, teacherSaveStage);
        teacherSaveStage.show();
    }

    @FXML
    public void onEditButtonClicked() {
        int index = teacherListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран преподаватель для редактирования!");
            noSelectedItemAlert.show();
            return;
        }
        Teacher teacher = teachers.get(index);
        Stage teacherSaveStage = TransitionFacade.getTeacherTransitionService().createTeacherSaveStage(currentStage, "Редактирование преподавателя", teacher, this);
        changePositionOfStage(currentStage, teacherSaveStage);
        teacherSaveStage.show();
    }

    @FXML
    public void onDeleteButtonClicked() {
        Teacher teacher = teacherListView.getSelectionModel().getSelectedItem();
        if (teacher != null) {
            Alert confirmationAlert = DialogFactory.createConfirmationAlert("Вы уверены, что хотите удалить данного преподавателя?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (DaoFacade.getTeacherDAOService().delete(teacher)) {
                    teachers.remove(teacher);
                } else {
                    Alert deletingErrorAlert = DialogFactory.createDeletingErrorAlert("Невозможно удалить этого преподавателя!");
                    deletingErrorAlert.show();
                }
            }
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран преподаватель для удаления!");
            noSelectedItemAlert.show();
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}