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
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentWindowController extends AbstractController implements RefreshableController {

    @FXML
    private ListView<Student> studentListView;
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage currentStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentListView.setItems(students);
        students.setAll(DaoFacade.getStudentDAOService().getList());
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
        students.setAll(DaoFacade.getStudentDAOService().getList());
    }


    @FXML
    public void onShowStatisticsButtonClicked() {
        Student student = studentListView.getSelectionModel().getSelectedItem();
        if (student != null) {
            Stage studentStatisticsStage = TransitionFacade.getStudentTransitionService().createStudentStatisticsStage(currentStage, student);
            changePositionOfStage(currentStage, studentStatisticsStage);
            studentStatisticsStage.show();
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран студент!");
            noSelectedItemAlert.show();
        }
    }

    @FXML
    public void onAddButtonClicked() {
        Student student = new Student();
        Stage studentSaveStage = TransitionFacade.getStudentTransitionService().createStudentSaveStage(currentStage, "Добавление студента", student, this);
        changePositionOfStage(currentStage, studentSaveStage);
        studentSaveStage.show();
    }

    @FXML
    public void onEditButtonClicked() {
        int index = studentListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран студент для редактирования!");
            noSelectedItemAlert.show();
            return;
        }
        Student student = students.get(index);
        Stage studentSaveStage = TransitionFacade.getStudentTransitionService().createStudentSaveStage(currentStage, "Редактирование студента", student, this);
        changePositionOfStage(currentStage, studentSaveStage);
        studentSaveStage.show();
    }

    @FXML
    public void onDeleteButtonClicked() {
        Student student = studentListView.getSelectionModel().getSelectedItem();
        if (student != null) {
            Alert confirmationAlert = DialogFactory.createConfirmationAlert("Вы уверены, что хотите удалить выбранного студента?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (DaoFacade.getStudentDAOService().delete(student)) {
                    students.remove(student);
                } else {
                    Alert deletingErrorAlert = DialogFactory.createDeletingErrorAlert("Невозможно удалить этого студента!");
                    deletingErrorAlert.show();
                }
            }
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбран студент для удаления!");
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