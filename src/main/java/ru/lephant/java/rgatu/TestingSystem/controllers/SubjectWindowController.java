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
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SubjectWindowController implements Initializable {

    @FXML
    private ListView<Subject> subjectListView;
    private ObservableList<Subject> subjects = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage modalStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectListView.setItems(subjects);
        subjects.setAll(DaoFacade.getSubjectDAOService().getList());
    }


    @FXML
    public void onAddButtonClicked() {
        Subject subject = new Subject();
        boolean needToSave = showSubjectChangingDialog("Добавление предмета", subject);
        if (needToSave) {
            DaoFacade.getSubjectDAOService().save(subject);
            subjects.add(subject);
        }
    }

    @FXML
    public void onEditButtonClicked() {
        int index = subjectListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            new NoSelectedItemAlert("Не выбран предмет для редактирования!");
            return;
        }
        Subject subject = subjects.get(index);
        boolean needToSave = showSubjectChangingDialog("Редактирование предмета", subject);
        if (needToSave) {
            DaoFacade.getSubjectDAOService().save(subject);
            subjects.set(index, subject);
        }
    }

    @FXML
    public void onDeleteButtonClicked() {
        Subject subject = subjectListView.getSelectionModel().getSelectedItem();
        if (subject != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранный предмет?");
            alert.setContentText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                DaoFacade.getSubjectDAOService().delete(subject);
                subjects.remove(subject);
                alert.close();
            } else {
                alert.close();
            }
        } else {
            new NoSelectedItemAlert("Не выбран предмет для удаления!");
        }
    }


    private boolean showSubjectChangingDialog(String title, Subject subject) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/subject_save_window.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(modalStage);
            dialogStage.setScene(new Scene(root));

            SubjectSaveWindowController subjectSaveWindowController = loader.getController();
            subjectSaveWindowController.setModalStage(dialogStage);
            subjectSaveWindowController.setSubject(subject);
            subjectSaveWindowController.fillFields();

            dialogStage.showAndWait();

            return subjectSaveWindowController.isNeedToSave();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }
}