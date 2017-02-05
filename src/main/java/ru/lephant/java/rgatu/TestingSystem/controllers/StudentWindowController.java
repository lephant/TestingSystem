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
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentWindowController implements Initializable {

    @FXML
    private ListView<Student> studentListView;
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage modalStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentListView.setItems(students);
        initStudents();
    }


    @FXML
    public void onShowStatisticsButtonClicked() {
        Student student = studentListView.getSelectionModel().getSelectedItem();
        if (student != null) {
            showStudentStatisticsStage(student);
        } else {
            showNoSelectedStudentAlert("Не выбран студент!");
        }

    }

    @FXML
    public void onAddButtonClicked() {
        Student student = new Student();
        boolean needToSave = showStudentChangingDialog("Добавление студента", student);
        if (needToSave) {
            saveStudent(student);
            students.add(student);
        }
    }

    @FXML
    public void onEditButtonClicked() {
        int index = studentListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showNoSelectedStudentAlert("Не выбран студент для редактирования!");
            return;
        }
        Student student = students.get(index);
        boolean needToSave = showStudentChangingDialog("Редактирование студента", student);
        if (needToSave) {
            saveStudent(student);
            students.set(index, student);
        }
    }

    private void showNoSelectedStudentAlert(String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка!");
        alert.setHeaderText(headerText);
        alert.setContentText(null);
        alert.show();
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
                deleteStudent(student);
                students.remove(student);
                alert.close();
            } else {
                alert.close();
            }
        } else {
            showNoSelectedStudentAlert("Не выбран студент для удаления!");
        }
    }


    private boolean showStudentChangingDialog(String title, Student student) {
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
            studentSaveWindowController.fillFields();

            dialogStage.showAndWait();

            return studentSaveWindowController.isNeedToSave();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveStudent(Student student) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(student);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void deleteStudent(Student student) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(student);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initStudents() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List<Student> list = session.createCriteria(Student.class).list();
            students.setAll(list);
        } finally {
            if (session != null) {
                session.close();
            }
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
            stage.initOwner(mainStage.getScene().getWindow());
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