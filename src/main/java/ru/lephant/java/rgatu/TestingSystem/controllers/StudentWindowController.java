package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.interfaces.PostInitializable;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentWindowController implements Initializable, RefreshableController, PostInitializable {

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
        currentStage.setOnCloseRequest(event -> mainStage.show());
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
            studentStatisticsStage.show();
        } else {
            new NoSelectedItemAlert("Не выбран студент!");
        }
    }

    @FXML
    public void onAddButtonClicked() {
        Student student = new Student();
        Stage studentSaveStage = TransitionFacade.getStudentTransitionService().createStudentSaveStage(currentStage, "Добавление студента", student, this);
        studentSaveStage.show();
    }

    @FXML
    public void onEditButtonClicked() {
        int index = studentListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            new NoSelectedItemAlert("Не выбран студент для редактирования!");
            return;
        }
        Student student = students.get(index);
        Stage studentSaveStage = TransitionFacade.getStudentTransitionService().createStudentSaveStage(currentStage, "Редактирование студента", student, this);
        studentSaveStage.show();
    }

    @FXML
    public void onDeleteButtonClicked() {
        Student student = studentListView.getSelectionModel().getSelectedItem();
        if (student != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранного студента?");
            alert.setContentText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DaoFacade.getStudentDAOService().delete(student);
                students.remove(student);
                alert.close();
            } else {
                alert.close();
            }
        } else {
            new NoSelectedItemAlert("Не выбран студент для удаления!");
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}