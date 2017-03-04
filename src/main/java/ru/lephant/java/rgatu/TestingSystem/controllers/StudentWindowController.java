package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentWindowController implements Initializable, RefreshableController {

    @FXML
    private ListView<Student> studentListView;
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage modalStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentListView.setItems(students);
        students.setAll(DaoFacade.getStudentDAOService().getList());
    }

    @Override
    public void refreshData() {
        students.setAll(DaoFacade.getStudentDAOService().getList());
    }


    @FXML
    public void onShowStatisticsButtonClicked() {
        Student student = studentListView.getSelectionModel().getSelectedItem();
        if (student != null) {
            showStudentStatisticsStage(student);
        } else {
            new NoSelectedItemAlert("Не выбран студент!");
        }
    }

    @FXML
    public void onAddButtonClicked() {
        Student student = new Student();
        showStudentChangingDialog("Добавление студента", student);
    }

    @FXML
    public void onEditButtonClicked() {
        int index = studentListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            new NoSelectedItemAlert("Не выбран студент для редактирования!");
            return;
        }
        Student student = students.get(index);
        showStudentChangingDialog("Редактирование студента", student);
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
            if (result.get() == ButtonType.OK) {
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


    private void showStudentChangingDialog(String title, Student student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_save_window.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(modalStage);
            dialogStage.setScene(new Scene(root));

            StudentSaveWindowController studentSaveWindowController = loader.getController();
            studentSaveWindowController.setModalStage(dialogStage);
            studentSaveWindowController.setStudent(student);
            studentSaveWindowController.setParentController(this);
            studentSaveWindowController.postInitialize();

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showStudentStatisticsStage(Student student) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_statistics_window.fxml"));
            Parent root = loader.load();

            StudentStatisticsWindowController studentStatisticsWindowController = loader.getController();
            studentStatisticsWindowController.setMainStage(mainStage);
            studentStatisticsWindowController.setModalStage(stage);
            studentStatisticsWindowController.setStudent(student);
            studentStatisticsWindowController.fillContent();

            stage.setScene(new Scene(root));
            stage.setTitle("Статистика студента");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(400D);
            stage.setHeight(400D);
            stage.setMinWidth(400D);
            stage.setMinHeight(400D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(modalStage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }
}