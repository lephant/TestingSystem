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
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Teacher;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TeacherWindowController implements Initializable {

    @FXML
    private ListView<Teacher> teacherListView;
    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage modalStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherListView.setItems(teachers);
        initTeachers();
    }


    @FXML
    public void onAddButtonClicked() {
        Teacher teacher = new Teacher();
        boolean needToSave = showTeacherChangingDialog("Добавление группы", teacher);
        if (needToSave) {
            saveTeacher(teacher);
            teachers.add(teacher);
        }
    }

    @FXML
    public void onEditButtonClicked() {
        int index = teacherListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            new NoSelectedItemAlert("Не выбран преподаватель для редактирования!");
            return;
        }
        Teacher teacher = teachers.get(index);
        boolean needToSave = showTeacherChangingDialog("Редактирование преподавателя", teacher);
        if (needToSave) {
            saveTeacher(teacher);
            teachers.set(index, teacher);
        }
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
                deleteTeacher(teacher);
                teachers.remove(teacher);
                alert.close();
            } else {
                alert.close();
            }
        } else {
            new NoSelectedItemAlert("Не выбран преподаватель для удаления!");
        }
    }


    private boolean showTeacherChangingDialog(String title, Teacher teacher) {
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
            teacherSaveWindowController.fillFields();

            dialogStage.showAndWait();

            return teacherSaveWindowController.isNeedToSave();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveTeacher(Teacher teacher) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(teacher);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void deleteTeacher(Teacher teacher) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(teacher);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initTeachers() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List<Teacher> list = session.createCriteria(Teacher.class).list();
            teachers.setAll(list);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }
}