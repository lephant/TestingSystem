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
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TeacherWindowController implements Initializable, RefreshableController {

    @FXML
    private ListView<Teacher> teacherListView;
    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage modalStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherListView.setItems(teachers);
        teachers.setAll(DaoFacade.getTeacherDAOService().getList());
    }

    @Override
    public void refreshData() {
        teachers.setAll(DaoFacade.getTeacherDAOService().getList());
    }


    @FXML
    public void onAddButtonClicked() {
        Teacher teacher = new Teacher();
        showTeacherChangingDialog("Добавление группы", teacher);
    }

    @FXML
    public void onEditButtonClicked() {
        int index = teacherListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            new NoSelectedItemAlert("Не выбран преподаватель для редактирования!");
            return;
        }
        Teacher teacher = teachers.get(index);
        showTeacherChangingDialog("Редактирование преподавателя", teacher);
    }

    @FXML
    public void onDeleteButtonClicked() {
        Teacher teacher = teacherListView.getSelectionModel().getSelectedItem();
        if (teacher != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText("Вы уверены, что хотите удалить данного преподавателя?");
            alert.setContentText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DaoFacade.getTeacherDAOService().delete(teacher);
                teachers.remove(teacher);
                alert.close();
            } else {
                alert.close();
            }
        } else {
            new NoSelectedItemAlert("Не выбран преподаватель для удаления!");
        }
    }


    private void showTeacherChangingDialog(String title, Teacher teacher) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/teacher_save_window.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(modalStage);
            dialogStage.setScene(new Scene(root));

            TeacherSaveWindowController teacherSaveWindowController = loader.getController();
            teacherSaveWindowController.setModalStage(dialogStage);
            teacherSaveWindowController.setTeacher(teacher);
            teacherSaveWindowController.setParentController(this);
            teacherSaveWindowController.postInitialize();

            dialogStage.show();
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